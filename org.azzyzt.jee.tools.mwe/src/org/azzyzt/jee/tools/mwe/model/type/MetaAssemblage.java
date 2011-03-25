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

public abstract class MetaAssemblage extends MetaType {
	
	private List<MetaType> memberTypes = new ArrayList<MetaType>();
	
	protected MetaAssemblage(String name) {
		super(name);
	}
	
	public abstract int maximumNumberOfMemberTypes();
	public abstract boolean isAcceptableMemberType(MetaType mt);

	public MetaType getMemberType(int index) {
		return memberTypes.get(index);
	}

	public void addMemberType(MetaType mt) {
		if (memberTypes.contains(mt)) return;
		
		if (memberTypes.size() == maximumNumberOfMemberTypes()) {
			throw new ToolError("Maximum number of member types for "+this+" reached, can't add any more");
		}
		if (isAcceptableMemberType(mt)) {
			memberTypes.add(mt);
		} else {
			throw new ToolError(mt+" is not an acceptable member type for "+this);
		}
	}

	public List<MetaType> getMemberTypes() {
		return memberTypes;
	}
	
	public int getMemberTypeCount() {
		return memberTypes.size();
	}
}
