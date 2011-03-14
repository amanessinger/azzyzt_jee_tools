package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;

public class DefaultStandardEntityListenersBuilder implements GenericBuilder {
	
	private static final String TEMPLATE_GROUP = "javaDefaultStandardEntityListenersGroup";

	private static final String CLASS_NAME = "StandardEntityListeners";
	
	private String packageName;
	private String simpleName;
	private MetaModel targetModel;
	private MetaStandardDefs std;

	public DefaultStandardEntityListenersBuilder(String packageName) {
		this.packageName = packageName;
		this.simpleName = CLASS_NAME;
		this.targetModel = new MetaModel();
		this.std = new MetaStandardDefs();
	}
	
	@Override
	public MetaModel build() {
		MetaClass target = MetaClass.forName(packageName, simpleName);
		target.setModifiers(std.mod_public);
		target.setSuperMetaClass(std.entityListenerBase);
		target.addReferencedForeignType(std.javaUtilHashMap);
		target.addReferencedForeignType(std.javaUtilMap);
		target.addReferencedForeignType(std.javaTextSimpleDateFormat);
		target.addReferencedForeignType(std.javaLangReflectMethod);
		target.addReferencedForeignType(std.javaxPersistencePrePersist);
		target.addReferencedForeignType(std.javaxPersistencePreUpdate);
		target.addReferencedForeignType(std.dateFieldType);
		
		targetModel.follow(packageName);
		targetModel.addMetaDeclaredTypeIfTarget(target);
		
		return targetModel;
	}

	@Override
	public String getTemplateGroup() {
		return TEMPLATE_GROUP;
	}

	@Override
	public boolean getGenerateFields() {
		return false;
	}

	@Override
	public boolean getGenerateDefaultConstructor() {
		return false;
	}

	@Override
	public boolean getGenerateGettersSetters() {
		return false;
	}

}
