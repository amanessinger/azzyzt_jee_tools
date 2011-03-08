package org.azzyzt.jee.runtime.dto.query;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({FieldReference.class, Literal.class})
public abstract class Operand {
	
	public abstract boolean isValid();

}
