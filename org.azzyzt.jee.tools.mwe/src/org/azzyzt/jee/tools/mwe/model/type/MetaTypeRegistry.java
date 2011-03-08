package org.azzyzt.jee.tools.mwe.model.type;

import java.util.HashMap;
import java.util.logging.Logger;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaTypeRegistry {
	
	private static Logger logger = Logger.getLogger(MetaTypeRegistry.class.getPackage().getName());
	
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
	
	public static void dump() {
		for (MetaTypeId id : reg.keySet()) {
			logger.finest("MetaTypeRegistry: got type "+reg.get(id).getClass().getSimpleName()+" "+id);
		}
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
