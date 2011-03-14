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
