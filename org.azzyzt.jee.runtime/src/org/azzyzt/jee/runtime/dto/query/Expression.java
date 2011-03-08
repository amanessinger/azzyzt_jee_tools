package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({//FieldValueExpression.class, 
	And.class, Or.class, Not.class, UnaryFieldExpression.class, BinaryFieldExpression.class})
public abstract class Expression implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Expression() { }

	public abstract boolean isValid();
	
	public abstract Expression getReplaceableBy();
	
}
