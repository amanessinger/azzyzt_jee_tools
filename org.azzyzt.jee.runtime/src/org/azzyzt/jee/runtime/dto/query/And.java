package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;

public class And extends BinaryBooleanExpression implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public And() { }
	
	public And(Expression ...expressions) {
		for (Expression e : expressions) {
			add(e);
		}
	}

}
