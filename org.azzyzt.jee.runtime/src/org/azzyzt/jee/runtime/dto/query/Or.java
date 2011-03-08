package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;

public class Or extends BinaryBooleanExpression implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Or() { }
	
	public Or(Expression ...expressions) {
		for (Expression e : expressions) {
			add(e);
		}
	}

}
