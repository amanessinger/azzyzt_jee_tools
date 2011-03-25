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

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaWildcard extends MetaType {

	private List<MetaType> upperBounds = new ArrayList<MetaType>();
	private List<MetaType> lowerBounds = new ArrayList<MetaType>();

	public static MetaWildcard forName(String name) {
		MetaWildcard result = (MetaWildcard)MetaTypeRegistry.metaTypeForName(name);
		if (result == null) {
			result = new MetaWildcard(name);
		}
		return result;
	}
	
	protected MetaWildcard(String name) {
		super(name);
	}

	public List<MetaType> getUpperBounds() {
		return upperBounds;
	}

	public void addUpperBound(MetaType mdt) {
		this.upperBounds.add(mdt);
	}

	public List<MetaType> getLowerBounds() {
		return lowerBounds;
	}

	public void addLowerBound(MetaType mdt) {
		this.lowerBounds.add(mdt);
	}

	@Override
	public String getShortName() {
		if (upperBounds.isEmpty() && lowerBounds.isEmpty()) {
			return getName();
		} else if (lowerBounds.isEmpty() && !upperBounds.isEmpty() && upperBounds.get(0).getName().equals(Object.class.getName())) {
			return getName();
		} else if (lowerBounds.isEmpty() && !upperBounds.isEmpty()) {
			return "? extends "+upperBounds.get(0).getShortName();
		} else if (upperBounds.isEmpty() && !lowerBounds.isEmpty()) {
			return "? super "+lowerBounds.get(0).getShortName();
		} else {
			throw new ToolError("A bounded wildcard can either have a lower or an upper bound, not both");
		}
	}

}
