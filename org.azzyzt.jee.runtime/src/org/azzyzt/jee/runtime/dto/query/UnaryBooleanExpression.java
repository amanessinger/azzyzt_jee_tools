package org.azzyzt.jee.runtime.dto.query;

import org.azzyzt.jee.runtime.exception.QuerySyntaxException;


public abstract class UnaryBooleanExpression extends Expression implements Node {
	
	private static final long serialVersionUID = 1L;
	private Expression expression;
	
	public Expression getExpression() {
		return expression;
	}

	public void add(Expression e) throws QuerySyntaxException {
		if (this.expression != null) {
			throw new QuerySyntaxException("A QuerySpec can only have a single expression");
		}
		expression = e;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public boolean isValid() {
		return expression != null;
	}

	@Override
	public Expression getReplaceableBy() {
		return null;
	}

	@Override
	public String toString() {
		return "UnaryBooleanExpression [expression=" + expression + "]";
	}
}
