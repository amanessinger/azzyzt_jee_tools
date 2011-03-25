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

import java.util.HashMap;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaTypeRegistry {
	
	private static HashMap<MetaTypeId, MetaType> reg = new HashMap<MetaTypeId, MetaType>();
	
	public static void add(MetaType mt) {
		if (reg.containsKey(mt.getId())) {
			throw new ToolError("Duplicate type: "+mt.getId());
		}
		reg.put(mt.getId(), mt);
	}
	
	public static void reset() {
		reg.clear();
	}
	
	public static MetaType byId(MetaTypeId id) {
		return reg.get(id);
	}
	
	public static MetaType metaTypeForName(String name) {
		MetaTypeId theId = new MetaTypeId(name);
		
		MetaType mt = byId(theId);
		if (mt != null) {
			return mt;
		}
		return null;
	}
	
}
