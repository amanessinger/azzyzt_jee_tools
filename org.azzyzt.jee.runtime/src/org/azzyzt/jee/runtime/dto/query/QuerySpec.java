package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.azzyzt.jee.runtime.exception.QuerySyntaxException;
import org.azzyzt.jee.runtime.util.AttributedTags2QuerySpec;
import org.azzyzt.jee.runtime.util.REST2QuerySpec;

@XmlRootElement(name="query_spec")
public class QuerySpec implements Node, Serializable {
	
	private static final long serialVersionUID = 1L;

	private Expression expression;
	
	private List<OrderByClause> orderByList = new ArrayList<OrderByClause>();
	
	public QuerySpec() { }
	
	public QuerySpec(Expression e, List<OrderByClause> orderByList) {
		this.expression = e;
		this.orderByList = orderByList;
	}
	
	public static QuerySpec fromXML(String querySpecXml) 
		throws QuerySyntaxException 
	{
		REST2QuerySpec r2qs = new AttributedTags2QuerySpec();
		return r2qs.fromXML(querySpecXml);
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public void add(Expression expression) 
		throws QuerySyntaxException 
	{
		if (this.expression != null) {
			throw new QuerySyntaxException("A QuerySpec can only have a single expression");
		}
		this.expression = expression;
	}

	public List<OrderByClause> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<OrderByClause> orderByList) {
		this.orderByList = orderByList;
	}

	public void addOrderBy(OrderByClause orderBy) {
		this.orderByList.add(orderBy);
	}

	@Override
	public String toString() {
		return "QuerySpec [expression=" + expression + ", orderBy=" + orderByList
		+ "]";
	}

	@Override
	public Expression getReplaceableBy() {
		// makes no sense at top node, but is enforced by Node interface
		return null;
	}

	@Override
	public boolean isValid() {
		// may make sense at top node, is enforced by Node interface
		return false;
	}

}
