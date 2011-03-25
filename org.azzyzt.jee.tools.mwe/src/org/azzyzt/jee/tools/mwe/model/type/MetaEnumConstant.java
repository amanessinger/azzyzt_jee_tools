/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
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

package org.azzyzt.jee.tools.mwe.model.type;

public class MetaEnumConstant implements MetaValue {

	private String shortName;
	private String shortQualifiedName;
	private int ordinal;

	public MetaEnumConstant(String shortName, String shortQualifiedName, int ordinal) {
		super();
		this.shortName = shortName;
		this.shortQualifiedName = shortQualifiedName;
		this.ordinal = ordinal;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortQualifiedName() {
		return shortQualifiedName;
	}

	public void setShortQualifiedName(String shortQualifiedName) {
		this.shortQualifiedName = shortQualifiedName;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	@Override
	public Object getValue() {
		return ordinal;
	}

	@Override
	public String toString() {
		return shortQualifiedName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((shortQualifiedName == null) ? 0 : shortQualifiedName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaEnumConstant other = (MetaEnumConstant) obj;
		if (shortQualifiedName == null) {
			if (other.shortQualifiedName != null)
				return false;
		} else if (!shortQualifiedName.equals(other.shortQualifiedName))
			return false;
		return true;
	}

}
