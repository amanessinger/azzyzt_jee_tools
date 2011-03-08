package org.azzyzt.jee.runtime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.azzyzt.jee.runtime.dto.query.And;
import org.azzyzt.jee.runtime.dto.query.BinaryBooleanExpression;
import org.azzyzt.jee.runtime.dto.query.BinaryFieldExpression;
import org.azzyzt.jee.runtime.dto.query.BinaryFieldOperator;
import org.azzyzt.jee.runtime.dto.query.Expression;
import org.azzyzt.jee.runtime.dto.query.FieldExpression;
import org.azzyzt.jee.runtime.dto.query.FieldReference;
import org.azzyzt.jee.runtime.dto.query.Literal;
import org.azzyzt.jee.runtime.dto.query.Not;
import org.azzyzt.jee.runtime.dto.query.Operand;
import org.azzyzt.jee.runtime.dto.query.Or;
import org.azzyzt.jee.runtime.dto.query.OrderByClause;
import org.azzyzt.jee.runtime.dto.query.QuerySpec;
import org.azzyzt.jee.runtime.dto.query.UnaryBooleanExpression;
import org.azzyzt.jee.runtime.dto.query.UnaryFieldExpression;
import org.azzyzt.jee.runtime.entity.EntityBase;
import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.exception.InvalidFieldException;
import org.azzyzt.jee.runtime.exception.NotYetImplementedException;
import org.azzyzt.jee.runtime.exception.QuerySyntaxException;
import org.azzyzt.jee.runtime.exception.ThisCantHappen;
import org.azzyzt.jee.runtime.meta.AssociationInfo;
import org.azzyzt.jee.runtime.meta.AssociationPathInfo;
import org.azzyzt.jee.runtime.meta.RequiredSelectionType;

public class QueryBuilder <ID, T extends EntityBase<ID>> {
	
	private EntityManager em;
	private QuerySpec qs;
	private Class<T> clazz;
	private TypeMetaInfo tmi;
	private CriteriaBuilder cb;
	private CriteriaQuery<T> cq;
	private Root<T> root;
	private boolean forceDistinct = false;
	private Map<String, From<?,?>> fieldOwnersByAssociationId = new HashMap<String, From<?,?>>();

	public QueryBuilder(EntityManager em, QuerySpec qs, Class<T> clazz, TypeMetaInfo tmi) {
		this.em = em;
		this.qs = new QueryOptimizer().optimize(qs);
		this.clazz = clazz;
		this.tmi = tmi;
		this.cb = em.getCriteriaBuilder();
		this.cq = cb.createQuery(clazz);
		this.root = cq.from(clazz);
	}

	public TypedQuery<T> build() 
	    throws InvalidFieldException, AccessDeniedException, QuerySyntaxException, NotYetImplementedException 
	{
		TypedQuery<T> q = null;
		
		Expression expr = qs.getExpression();
		if (expr != null) {
			Predicate p = process(expr);
			cq.where(p);
		}
		List<OrderByClause> orderByList = qs.getOrderByList();
		if (orderByList != null && orderByList.size() > 0) {
			List<Order> orderList = new ArrayList<Order>();
			for (OrderByClause orderBy : orderByList) {
				String fieldName = orderBy.getFieldName();
				
				javax.persistence.criteria.Expression<?> nameExpr = toNameExpression(fieldName);

			    if (orderBy.isAscending()) {
					orderList.add(cb.asc(nameExpr));
				} else {
					orderList.add(cb.desc(nameExpr));
				}
			}
			cq.orderBy(orderList);
		}
		if (forceDistinct) {
			cq.distinct(true);
		}
		q = em.createQuery(cq);
		
		return q;
	}

	private javax.persistence.criteria.Expression<?> toNameExpression(String fieldName)
			throws InvalidFieldException, AccessDeniedException 
	{
		tmi.fieldVerification(clazz, fieldName);
		Class<?> fieldType = tmi.getFieldType(clazz, fieldName);

		From<?,?> fieldOwner;
		String fieldSelector;
		if (tmi.isAssociationPath(fieldName)) {
			AssociationPathInfo api = tmi.getValidPaths().get(clazz, fieldName);
			ensureJoins(api);
			List<AssociationInfo> ais = api.getAssociationInfos();
			// last join is field owner
			AssociationInfo lastAi = ais.get(ais.size() - 1);
			fieldOwner = fieldOwnersByAssociationId.get(lastAi.getId());
			fieldSelector = api.getFieldSelector();
		} else {
			fieldOwner = root;
			fieldSelector = fieldName;
		}

		javax.persistence.criteria.Expression<?> nameExpr = 
			fieldOwner.get(fieldSelector).as(fieldType);
		
		return nameExpr;
	}
	
	private void ensureJoins(AssociationPathInfo api) {
		From<?,?> from = root;
		for (AssociationInfo ai : api.getAssociationInfos()) {
			String id = ai.getId();
			if (fieldOwnersByAssociationId.containsKey(id)) {
				from = fieldOwnersByAssociationId.get(id);
				continue;
			}
			From<?,?> join;
			String joinField = ai.getFieldSelector();
			switch (ai.getJoinType()) {
			case Simple:
				join = from.join(joinField);
				break;
			case List:
				join = from.joinList(joinField);
				break;
			case Set:
				join = from.joinSet(joinField);
				break;
			case Map:
				join = from.joinMap(joinField);
				break;
			default:
				throw new ThisCantHappen();
			}
			if (ai.isForcingDistinct().equals(RequiredSelectionType.Distinct)) {
				forceDistinct = true;
			}
			fieldOwnersByAssociationId.put(id, join);
			from = join;
		}
	}

	private Predicate process(Expression e) 
	    throws InvalidFieldException, AccessDeniedException, QuerySyntaxException, NotYetImplementedException 
	{
		Predicate p = null;
				
		if (e instanceof FieldExpression) {
			FieldExpression fe = (FieldExpression)e;
			
			if (e instanceof UnaryFieldExpression) {
				p = processUnaryFieldExpression((UnaryFieldExpression)e);
			} else if (e instanceof BinaryFieldExpression) {
				p = processBinaryFieldExpression((BinaryFieldExpression)e);
			} else {
				throw new NotYetImplementedException();
			}
			if (fe.isNegated()) {
				p = cb.not(p);
			}
		} else if (e instanceof BinaryBooleanExpression) {
			List<Expression> terms = ((BinaryBooleanExpression) e).getTerms();
			if (terms == null || terms.size() < 2) {
				throw new QuerySyntaxException("Binary operators must have at least two terms");
			}
			if (e instanceof And) {
				if (terms.size() == 2) {
					p = cb.and(process(terms.get(0)), process(terms.get(1)));
				} else {
					// chain them in pairs
					BinaryBooleanExpression rest = new And();
					rest.setTerms(terms.subList(1, terms.size()));
					p = cb.and(process(terms.get(0)), process(rest));
				}
			} else if (e instanceof Or) {
				if (terms.size() == 2) {
					p = cb.or(process(terms.get(0)), process(terms.get(1)));
				} else {
					// chain them in pairs
					BinaryBooleanExpression rest = new Or();
					rest.setTerms(terms.subList(1, terms.size()));
					p = cb.or(process(terms.get(0)), process(rest));
				}
			}
		} else if (e instanceof UnaryBooleanExpression){
			Expression subExpr = ((UnaryBooleanExpression) e).getExpression();
			if (e instanceof Not) {
				p = cb.not(process(subExpr));
			} else {
				throw new NotYetImplementedException();
			}
		} else {
			throw new QuerySyntaxException("Unsupported query expression type "+e.getClass().getSimpleName());
		}
		
		return p;
	}

	private Predicate processUnaryFieldExpression(UnaryFieldExpression ufe) 
		throws InvalidFieldException, AccessDeniedException, NotYetImplementedException 
	{
		javax.persistence.criteria.Expression<?> operand1Expr = toNameExpression(ufe.getFieldName());
		switch (ufe.getOp()) {
		case IS_NULL:
			return cb.isNull(operand1Expr);
		case IS_NOT_NULL:
			return cb.isNotNull(operand1Expr);
		default:
			throw new NotYetImplementedException();
		}
	}

	private Predicate processBinaryFieldExpression(BinaryFieldExpression bfe) 
		throws NotYetImplementedException, InvalidFieldException, AccessDeniedException 
	{
		String operand1FieldName = bfe.getFieldName();
		javax.persistence.criteria.Expression<?> operand1Expr = toNameExpression(operand1FieldName);
		Class<?> operand1Type = tmi.getFieldType(clazz, operand1FieldName);
		Operand operand2 = bfe.getOperand();
		BinaryFieldOperator operator = bfe.getOp();
		if (operand2 instanceof Literal) {
			if (String.class.isAssignableFrom(operand1Type)) {
				return processBinaryStringFieldValueExpression(operand1Expr, operator, (Literal)operand2, bfe.isCaseSensitive());
			} else {
				return processBinaryFieldValueExpression(operand1Expr, operand1Type, operator, (Literal)operand2);
			}
		} else if (operand2 instanceof FieldReference) {
			if (String.class.isAssignableFrom(operand1Type)) {
				return processBinaryStringFieldFieldExpression(operand1Expr, operator, (FieldReference)operand2, bfe.isCaseSensitive());
			} else {
				return processBinaryFieldFieldExpression(operand1Expr, operand1Type, operator, (FieldReference)operand2);
			}
		} else {
			throw new NotYetImplementedException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private Predicate processBinaryStringFieldValueExpression(
			javax.persistence.criteria.Expression<?> operand1Expr,
			BinaryFieldOperator operator,
			Literal operand2, 
			boolean isCaseSensitive
			) 
		throws NotYetImplementedException
	{
		javax.persistence.criteria.Expression<String> stringOperand1Expr 
			= (javax.persistence.criteria.Expression<String>)operand1Expr;
		String stringOperand2 = (String)operand2.getValue();
		if (!isCaseSensitive) {
			stringOperand1Expr = cb.upper(stringOperand1Expr);
			stringOperand2 = stringOperand2.toUpperCase();
		}
		if (operator == BinaryFieldOperator.LIKE) {
			return cb.like(stringOperand1Expr, stringOperand2);
		} else {
			return constructComparableBFVE(stringOperand1Expr, stringOperand2, operator);
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate processBinaryStringFieldFieldExpression(
			javax.persistence.criteria.Expression<?> operand1Expr,
			BinaryFieldOperator operator, 
			FieldReference operand2,
			boolean isCaseSensitive
			) 
		throws NotYetImplementedException, InvalidFieldException, AccessDeniedException
	{
		javax.persistence.criteria.Expression<String> stringOperand1Expr 
			= (javax.persistence.criteria.Expression<String>)operand1Expr;
		javax.persistence.criteria.Expression<String> stringOperand2Expr 
			= (javax.persistence.criteria.Expression<String>)toNameExpression(operand2.getFieldName());
		if (!isCaseSensitive) {
			stringOperand1Expr = cb.upper(stringOperand1Expr);
			stringOperand2Expr = cb.upper(stringOperand2Expr);
		}
		if (operator == BinaryFieldOperator.LIKE) {
			return cb.like(stringOperand1Expr, stringOperand2Expr);
		} else {
			return constructComparableBFFE(stringOperand1Expr, stringOperand2Expr, operator);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <C extends Comparable<? super C>> Predicate processBinaryFieldValueExpression(
			javax.persistence.criteria.Expression<?> operand1Expr, 
			Class<?> operand1Type, 
			BinaryFieldOperator operator, 
			Literal operand2
		)
		throws NotYetImplementedException
	{
		if (Comparable.class.isAssignableFrom(operand1Type)) {
			return constructComparableBFVE((javax.persistence.criteria.Expression<C>)operand1Expr, (C)operand2.getValue(), operator);
		} else {
			throw new NotYetImplementedException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private <C extends Comparable<? super C>> Predicate processBinaryFieldFieldExpression(
			javax.persistence.criteria.Expression<?> operand1Expr, 
			Class<?> operand1Type, 
			BinaryFieldOperator operator, 
			FieldReference operand2
		) 
		throws InvalidFieldException, AccessDeniedException, NotYetImplementedException 
	{
		javax.persistence.criteria.Expression<?> operand2Expr = toNameExpression(operand2.getFieldName());
		if (operator == BinaryFieldOperator.LIKE) {
			return cb.like((javax.persistence.criteria.Expression<String>)operand1Expr, (javax.persistence.criteria.Expression<String>)operand2Expr);
		} else if (Comparable.class.isAssignableFrom(operand1Type)) {
			return constructComparableBFFE((javax.persistence.criteria.Expression<C>)operand1Expr, (javax.persistence.criteria.Expression<C>)operand2Expr, operator);
		} else {
			throw new NotYetImplementedException();
		}
	}

	private <C extends Comparable<? super C>> Predicate constructComparableBFVE(
			javax.persistence.criteria.Expression<C> comparableFieldExpr, 
			C comparableValue,
			BinaryFieldOperator op
			) 
		throws NotYetImplementedException 
	{
		Predicate p;
		switch (op) {
		case EQ:
			p = cb.equal(comparableFieldExpr, comparableValue);
			break;
		case LT:
			p = cb.lessThan(comparableFieldExpr, comparableValue);
			break;
		case LE:
			p = cb.lessThanOrEqualTo(comparableFieldExpr, comparableValue);
			break;
		case GT:
			p = cb.greaterThan(comparableFieldExpr, comparableValue);
			break;
		case GE:
			p = cb.greaterThanOrEqualTo(comparableFieldExpr, comparableValue);
			break;
		default:
			throw new NotYetImplementedException();
		}
		return p;
	}

	private <C extends Comparable<? super C>> Predicate constructComparableBFFE(
			javax.persistence.criteria.Expression<C> comparableFieldExpr1, 
			javax.persistence.criteria.Expression<C> comparableFieldExpr2, 
			BinaryFieldOperator op
			) 
		throws NotYetImplementedException 
	{
		Predicate p;
		switch (op) {
		case EQ:
			p = cb.equal(comparableFieldExpr1, comparableFieldExpr2);
			break;
		case LT:
			p = cb.lessThan(comparableFieldExpr1, comparableFieldExpr2);
			break;
		case LE:
			p = cb.lessThanOrEqualTo(comparableFieldExpr1, comparableFieldExpr2);
			break;
		case GT:
			p = cb.greaterThan(comparableFieldExpr1, comparableFieldExpr2);
			break;
		case GE:
			p = cb.greaterThanOrEqualTo(comparableFieldExpr1, comparableFieldExpr2);
			break;
		default:
			throw new NotYetImplementedException();
		}
		return p;
	}

}
