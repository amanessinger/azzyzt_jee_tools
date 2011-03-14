package org.azzyzt.jee.tools.mwe.model.type;


public class MetaInterface extends MetaDeclaredType {

	public static MetaInterface forName(String packageName, String simpleName) {
		MetaInterface result = getOrConstruct(null, packageName, simpleName);
		return result;
	}
	
	public static MetaInterface forType(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String simpleName = clazz.getSimpleName();
		MetaInterface result = getOrConstruct(clazz, packageName, simpleName);
		return result;
	}

	protected static MetaInterface getOrConstruct(Class<?> clazz,
			String packageName, String simpleName) {
		MetaType metaType = MetaTypeRegistry.metaTypeForName(createFqName(packageName, simpleName));
		MetaInterface result = (MetaInterface)metaType;
		if (result == null || !(result instanceof MetaInterface)) {
			result = new MetaInterface(clazz, packageName, simpleName);
			result.postConstructionAnalysis();
		}
		return result;
	}
	
	protected MetaInterface(Class<?> clazz, String packageName, String simpleName) {
		super(clazz, packageName, simpleName);
	}

	@Override
	public void postConstructionAnalysis() {
		super.postConstructionAnalysis();
		Class<?> clazz = getClazz();
		if (isTarget() && clazz != null) {
			analyzeMethods(clazz);
		}
	}

	@Override
	public int compareTo(MetaDeclaredType other) {
		return getFqName().compareTo(other.getFqName());
	}
}
