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

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaBuiltin extends MetaType {
	
	public static MetaBuiltin forName(String name) {
		MetaBuiltin result = (MetaBuiltin)MetaTypeRegistry.metaTypeForName(name);
		if (result == null) {
			result = new MetaBuiltin(name);
		}
		return result;
	}
	
	protected MetaBuiltin(String name) {
		super(name);
	}

	public MetaClass getBoxedType() {
		MetaClass result;
		if (getName().equals("boolean")) {
			result = MetaClass.forType(java.lang.Boolean.class);
		} else if (getName().equals("byte")) {
			result = MetaClass.forType(java.lang.Byte.class);
		} else if (getName().equals("char")) {
			result = MetaClass.forType(java.lang.Character.class);
		} else if (getName().equals("short")) {
			result = MetaClass.forType(java.lang.Short.class);
		} else if (getName().equals("int")) {
			result = MetaClass.forType(java.lang.Integer.class);
		} else if (getName().equals("long")) {
			result = MetaClass.forType(java.lang.Long.class);
		} else if (getName().equals("float")) {
			result = MetaClass.forType(java.lang.Float.class);
		} else if (getName().equals("double")) {
			result = MetaClass.forType(java.lang.Double.class);
		} else {
			throw new ToolError("Builtin type "+getName()+" has no boxed type");
		}
		return result;
	}

	@Override
	public boolean isBuiltinType() {
		return true;
	}
}
