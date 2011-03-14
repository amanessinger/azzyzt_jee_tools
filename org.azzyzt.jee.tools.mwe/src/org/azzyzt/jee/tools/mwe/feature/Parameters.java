package org.azzyzt.jee.tools.mwe.feature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class Parameters {
	
	private Map<String, Parameter> parameterMap = new HashMap<String, Parameter>();
	
	public void add(Parameter p) {
		if (parameterMap.containsKey(p.getName())) {
			throw new ToolError("parameter map already contains parameter "+p.getName());
		}
		parameterMap.put(p.getName(), p);
	}
	
	public Parameter byName(String name) {
		if (!parameterMap.containsKey(name)) {
			throw new ToolError("parameter map has no parameter "+name);
		}
		return parameterMap.get(name);
	}
	
	public Collection<Parameter> all() {
		return parameterMap.values();
	}

}
