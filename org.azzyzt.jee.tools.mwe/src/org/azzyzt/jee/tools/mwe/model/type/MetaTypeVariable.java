package org.azzyzt.jee.tools.mwe.model.type;

public class MetaTypeVariable extends MetaType {

	public static MetaTypeVariable forName(String name) {
		MetaTypeVariable result = (MetaTypeVariable)MetaTypeRegistry.metaTypeForName(name);
		if (result == null || !(result instanceof MetaTypeVariable)) {
			result = new MetaTypeVariable(name);
		}
		return result;
	}
	
	protected MetaTypeVariable(String name) {
		super(name);
	}
	
}
