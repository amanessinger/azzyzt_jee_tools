package org.azzyzt.jee.tools.mwe.builder;

public interface GenericBuilder extends Builder {

	public String getTemplateGroup();
	public boolean getGenerateFields();
	public boolean getGenerateDefaultConstructor();
	public boolean getGenerateGettersSetters();
	
}
