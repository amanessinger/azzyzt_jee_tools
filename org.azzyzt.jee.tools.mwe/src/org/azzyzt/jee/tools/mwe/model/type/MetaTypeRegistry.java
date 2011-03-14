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
