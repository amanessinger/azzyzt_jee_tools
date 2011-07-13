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

package org.azzyzt.jee.runtime.meta;

import java.util.HashMap;
import java.util.Map;

import org.azzyzt.jee.runtime.exception.ThisCantHappen;

public class ValidAssociationPathsBase {
	
	protected Map<Class<?>, Map<String, AssociationPathInfo>> validAssociationPathInfosByPath = new HashMap<Class<?>, Map<String, AssociationPathInfo>>();
	
	protected void add(Class<?> clazz, String path, AssociationPathInfo api) {
		Map<String, AssociationPathInfo> clazzEntry;
		if (validAssociationPathInfosByPath.containsKey(clazz)) {
			clazzEntry = validAssociationPathInfosByPath.get(clazz);
		} else {
			clazzEntry = new HashMap<String, AssociationPathInfo>();
			validAssociationPathInfosByPath.put(clazz, clazzEntry);
		}
		clazzEntry.put(path, api);
	}
	
	protected boolean contains(Class<?> clazz, String path) {
		if (!validAssociationPathInfosByPath.containsKey(clazz)) {
			return false;
		}
		Map<String, AssociationPathInfo> clazzEntry = validAssociationPathInfosByPath.get(clazz);
		return clazzEntry.containsKey(path);
	}
	
	protected AssociationPathInfo get(Class<?> clazz, String path) {
		if (!validAssociationPathInfosByPath.containsKey(clazz)) {
			throw new ThisCantHappen();
		}
		Map<String, AssociationPathInfo> clazzEntry = validAssociationPathInfosByPath.get(clazz);
		if (!clazzEntry.containsKey(path)) {
			throw new ThisCantHappen();
		}
		return clazzEntry.get(path);
	}	

}
