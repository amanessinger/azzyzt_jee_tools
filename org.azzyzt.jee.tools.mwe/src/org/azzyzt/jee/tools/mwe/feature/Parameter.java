package org.azzyzt.jee.tools.mwe.feature;

import java.io.File;
import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class Parameter {
	
	public static final boolean IS_OPTIONAL = true;
	public static final boolean IS_MANDATORY = false;
	
	private String name;
	private ParameterType type;
	private boolean isOptional;
	private Object value = null;
	
	public Parameter(String name, ParameterType type, boolean isOptional) {
		super();
		this.name = name;
		this.type = type;
		this.isOptional = isOptional;
	}

	public String getName() {
		return name;
	}

	public ParameterType getType() {
		return type;
	}

	public boolean isOptional() {
		return isOptional;
	}

	protected Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		switch (type) {
		case String:
			if (!(value instanceof String)) {
				throw new ToolError("Value for parameter "+name+" must be a string but is a "+value.getClass().getName());
			}
			// intentionally falling through
		case SourceFolder:
			File dir = new File((String) value);
			if (!dir.isDirectory() || ! dir.canWrite()) {
				throw new ToolError("Value for parameter "+name+" must be the name of a writable directory but is "+value);
			}
			break;
		case ListString:
			if (!(value instanceof List<?>)) {
				throw new ToolError("Value for parameter "+name+" must be a list of strings but is a "+value.getClass().getName());
			}
			break;
		default:
			throw new ToolError("Unsupported parameter type "+type);
		}
		this.value = value;
	}

}
