package org.azzyzt.jee.runtime.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author advman
 *
 */
public class Credential {
	
	private String name;
	private Map<String, String> properties = new HashMap<String, String>();
	static final String PROPVAL_TRUE = "true";
	
	public Credential(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean hasProperty(String propName) {
		return properties.containsKey(propName);
	}

	public String getPropertyValue(String propName) {
		return properties.get(propName);
	}
	
	public boolean isPropertyTrue(String propName) {
		return hasProperty(propName) && properties.get(propName).equalsIgnoreCase(PROPVAL_TRUE);
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

	public void mergeFrom(Credential other) {
		if (other == null) {
			return;
		}
		Set<String> propertyNames = other.propertyNames();
		if (propertyNames.isEmpty()) {
			return;
		}
		for (String propertyName : propertyNames) {
			// other's property value override this' value
			properties.put(propertyName, other.getPropertyValue(propertyName));
		}
	}

	public boolean satisfies(Credential required) {
		if (required == null) {
			return true;
		}
		Set<String> propertyNames = required.propertyNames();
		if (propertyNames.isEmpty()) {
			return true;
		}
		for (String propertyName : propertyNames) {
			if (!hasProperty(propertyName)) {
				return false;
			}
			if (!getPropertyValue(propertyName).equals(required.getPropertyValue(propertyName))) {
				return false;
			}
		}
		
		return true;
	}

}
