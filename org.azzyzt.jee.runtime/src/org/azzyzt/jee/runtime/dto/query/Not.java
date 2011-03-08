package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;

public class Not extends UnaryBooleanExpression implements Serializable {

	private static final long serialVersionUID = 1L;

	public Not() { }
	
	public Not(Expression e) {
		setExpression(e);
	}
	
}
