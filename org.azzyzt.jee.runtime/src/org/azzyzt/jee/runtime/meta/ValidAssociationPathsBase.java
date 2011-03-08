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
