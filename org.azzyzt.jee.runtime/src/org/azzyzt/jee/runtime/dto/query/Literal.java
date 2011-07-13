/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.runtime.dto.query;

import org.azzyzt.jee.runtime.exception.NotYetImplementedException;

/**
 * Literal occurring in an <code>Expression</code>. A <code>Literal</code> has a type and a value. 
 */
public class Literal extends Operand {
	
	public Literal() { }
	
	public Literal(LiteralType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	private LiteralType type;
	
	private Object value;

	public LiteralType getType() {
		return type;
	}

	public void setType(LiteralType type) {
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Factory method that creates a <code>Literal</code> from a text.
	 * @param text the input
	 * @param type supposed type of the input
	 * @return the <code>Literal</code>
	 * @throws NotYetImplementedException
	 */
	public static Literal parse(String text, LiteralType type) 
		throws NotYetImplementedException 
	{
		Literal l = new Literal();
		l.setType(type);
		
		if (text != null) {
			switch (type) {
			case STRING:
				l.setValue(text.trim());
				break;
			case SHORT:
				l.setValue(Short.parseShort(text));
				break;
			case INTEGER:
				l.setValue(Integer.parseInt(text));
				break;
			case LONG:
				l.setValue(Long.parseLong(text));
				break;
			case FLOAT:
				l.setValue(Float.parseFloat(text));
				break;
			case DOUBLE:
				l.setValue(Double.parseDouble(text));
				break;
			default:
				throw new NotYetImplementedException();
			}
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Operand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (type == null || value == null)
			return false;
		if (!type.clazz().isAssignableFrom(value.getClass()))
			return false;
		return true;
	}

}
