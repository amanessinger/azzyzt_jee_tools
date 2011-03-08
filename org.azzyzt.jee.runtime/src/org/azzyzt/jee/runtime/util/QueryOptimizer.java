package org.azzyzt.jee.runtime.util;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.runtime.dto.query.BinaryBooleanExpression;
import org.azzyzt.jee.runtime.dto.query.Expression;
import org.azzyzt.jee.runtime.dto.query.QuerySpec;
import org.azzyzt.jee.runtime.dto.query.UnaryBooleanExpression;

public class QueryOptimizer {
	
	public QuerySpec optimize(QuerySpec qs) {
		if (qs == null) return null;
		
		QuerySpec result = new QuerySpec(
				optimizeExpression(qs.getExpression()),
				qs.getOrderByList()
		);
		return result;
	}

	private Expression optimizeExpression(Expression expr) {
		if (expr == null) return null;
		
		Expression result;
		if (expr instanceof BinaryBooleanExpression) {
			BinaryBooleanExpression binOp = (BinaryBooleanExpression) expr;
			List<Expression> terms = binOp.getTerms();
			int numberOfTerms = terms.size();
			if (numberOfTerms == 0) {
				return null;
			} else if (numberOfTerms == 1) {
				return optimizeExpression(terms.get(0));
			}
			List<Expression> validTerms = new ArrayList<Expression>(numberOfTerms);
			boolean needsReevaluation = false;
			for (Expression term : terms) {
				if (term.isValid()) {
					validTerms.add(optimizeExpression(term));
				} else {
					needsReevaluation = true;
					Expression replacement = term.getReplaceableBy();
					if (replacement != null) {
						validTerms.add(optimizeExpression(replacement));
					}
				}
			}
			binOp.setTerms(validTerms);
			if (needsReevaluation) {
				result = optimizeExpression(binOp);
			} else {
				result = binOp;
			}
		} else if (expr instanceof UnaryBooleanExpression) {
			UnaryBooleanExpression unOp = (UnaryBooleanExpression) expr;
			Expression subExpr = unOp.getExpression();
			subExpr = optimizeExpression(subExpr);
			if (subExpr.isValid()) {
				unOp.setExpression(subExpr);
				return unOp;
			} else {
				return null;
			}
		} else {
			if (expr.isValid()) {
				result = expr;
			} else {
				result = null;
			}
		}
		return result;
	}

}
