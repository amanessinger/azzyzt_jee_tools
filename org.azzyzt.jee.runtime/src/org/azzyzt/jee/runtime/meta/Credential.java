package org.azzyzt.jee.runtime.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Credential {
	
	private String name;
	private Map<String, String> properties = new HashMap<String, String>();
	
	public Credential(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean hasProperty(Object propName) {
		return properties.containsKey(propName);
	}

	public String getValue(Object propName) {
		return properties.get(propName);
	}

	public Set<String> propertyNames() {
		return properties.keySet();
	}

	public String addProperty(String propName, String propVal) {
		return properties.put(propName, propVal);
	}

	public int propertyCount() {
		return properties.size();
	}

}
