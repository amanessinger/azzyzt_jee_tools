package org.azzyzt.jee.tools.mwe.model.type;

import java.util.Properties;

public class MetaMethodParameter {

	private String name;
	private MetaType type;
    private MetaModifiers modifiers; // TODO find out how to reflect param modifiers
	private Properties properties = new Properties(); // may be set by a synthesizing builder

	// TODO Introduce MetaAnnotations for parameters. We'll need to link back to the method though 
	
	public MetaMethodParameter(String name, MetaType type, int reflected) {
		super();
		initCommon(name, type);
		this.modifiers = new MetaModifiers(reflected);
	}

	public MetaMethodParameter(String name, MetaType type) {
		super();
		initCommon(name, type);
		this.modifiers = new MetaModifiers();
	}

	private void initCommon(String name, MetaType type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaType getType() {
		return type;
	}

	public void setType(MetaType type) {
		this.type = type;
	}

	public MetaModifiers getModifiers() {
		return modifiers;
	}

	public void setFinal() {
		this.modifiers.setFinal(true);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

}
