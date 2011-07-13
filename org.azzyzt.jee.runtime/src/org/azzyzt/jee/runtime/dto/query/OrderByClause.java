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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <code>QuerySpec</code> has a list of order-by clauses. They determine the 
 * sort order of a query result. Each clause refers to an entity field and 
 * sorting can either be ascending or descending.
 * 
 * @see QuerySpec
 */
@XmlRootElement(name = "orderby")
public class OrderByClause implements FieldReferer, Serializable {
	
	private static final long serialVersionUID = 1L;

	private String fieldName;
	private boolean isAscending;
	
	public OrderByClause() { }
	
	public OrderByClause(String fieldName, boolean isAscending) {
		super();
		this.fieldName = fieldName;
		this.isAscending = isAscending;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isAscending() {
		return isAscending;
	}

	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public String toString() {
		return "OrderByClause [fieldName=" + fieldName + ", isAscending=" + isAscending + "]";
	}
	
}
