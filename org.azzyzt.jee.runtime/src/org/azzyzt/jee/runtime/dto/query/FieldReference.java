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

/**
 * A <code>FieldReference</code> is an <code>Operand</code> in an <code>Expression</code>.
 * A field reference can be to a field of the result entity of the query (with the field
 * name exactly being the name of a field of that entity), or it can refer to a field in 
 * another entity that is reachable via a mapped association. In the latter case we use 
 * the same dotted notation as in JPQL.
 * 
 * @see Expression
 */
public class FieldReference extends Operand implements FieldReferer {
	
	private String fieldName;
	
	private boolean isCaseSensitive;

	public FieldReference() { }

	public FieldReference(String fieldName) {
		super();
		this.fieldName = fieldName;
		setCaseSensitive(false);
	}

	public FieldReference(String fieldName, boolean isCaseSensitive) {
		super();
		this.fieldName = fieldName;
		setCaseSensitive(isCaseSensitive);
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldReferer#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return fieldName;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldReferer#setFieldName(java.lang.String)
	 */
	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Operand#isValid()
	 */
	@Override
	public boolean isValid() {
		return fieldName != null && ! fieldName.isEmpty();
	}
}