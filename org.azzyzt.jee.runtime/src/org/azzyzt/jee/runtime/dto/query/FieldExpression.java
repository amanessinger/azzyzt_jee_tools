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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * <p>The abstract parent class for expressions that refer to entity fields. 
 * Field expressions can be negated and (if it makes sense, not checked)
 * they can be case-sensitive or insensitive.</p>
 * 
 * <p>A <code>FieldExpression</code> has at least one <code>FieldReference</code>.</p>
 * 
 * @see FieldReference
 */
@XmlSeeAlso({UnaryFieldExpression.class, BinaryFieldExpression.class})
public abstract class FieldExpression extends Expression implements FieldReferer, Serializable {

	private static final long serialVersionUID = 1L;

	private FieldReference fieldReference = new FieldReference();

	private boolean isNegated;
	
	public FieldExpression() { }
	
	public FieldExpression(FieldReference fieldReference, boolean isNegated) {
		super();
		this.fieldReference = fieldReference;
		this.isNegated = isNegated;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldReferer#getFieldName()
	 */
	@XmlElement(nillable=false, required=true)
	@Override
	public String getFieldName() {
		return fieldReference.getFieldName();
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldReferer#setFieldName(java.lang.String)
	 */
	@Override
	public void setFieldName(String fieldName) {
		this.fieldReference.setFieldName(fieldName);
	}

	@XmlAttribute
	public boolean isNegated() {
		return isNegated;
	}

	public void setNegated(boolean isNegated) {
		this.isNegated = isNegated;
	}

	public boolean isCaseSensitive() {
		return fieldReference.isCaseSensitive();
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		fieldReference.setCaseSensitive(isCaseSensitive);
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#getReplaceableBy()
	 */
	@Override
	public Expression getReplaceableBy() {
		// FEs are never replaceable, they are only reducible
		return null;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean result;
		String fieldName = getFieldName();
		result =  fieldName != null 
			   && fieldName.length() > 0;
		return result;
	}

}
