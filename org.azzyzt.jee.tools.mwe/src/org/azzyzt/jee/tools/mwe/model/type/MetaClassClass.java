package org.azzyzt.jee.tools.mwe.model.type;

public class MetaClassClass extends MetaType {

	private MetaType parameterType = null;
	private String shortTypeParameter;
	
	public static MetaClassClass forMetaType(MetaType argumentType) {
		String name = "Class<"+argumentType.getName()+">";
		MetaClassClass result = (MetaClassClass)MetaTypeRegistry.metaTypeForName(name);
		if (result == null) {
			String shortTypeParameter = argumentType.getShortName();
			result = new MetaClassClass(name, shortTypeParameter);
		}
		return result;
	}
	
	protected MetaClassClass(String name, String shortTypeParameter) {
		super(name);
		this.shortTypeParameter = shortTypeParameter;
	}

	public boolean isParametrized() {
		return parameterType != null;
	}

	protected MetaType getParameterType() {
		return parameterType;
	}

	protected void setParameterType(MetaType parameterType) {
		this.parameterType = parameterType;
	}

	@Override
	public String getShortName() {
		return "Class<"+shortTypeParameter+">";
	}
}
