package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;

public class InitialStandardEntityListenerBuilder implements GenericBuilder {
	
	private static final String TEMPLATE_GROUP = "javaHelloServiceBeanGroup";

	private static final String HELLO_BEAN_NAME = "HelloTestBean";
	
	private String packageName;
	private String simpleName;
	private MetaModel targetModel;
	private MetaStandardDefs std;

	public InitialStandardEntityListenerBuilder(String packageName) {
		this.packageName = packageName;
		this.simpleName = HELLO_BEAN_NAME;
		this.targetModel = new MetaModel();
		this.std = new MetaStandardDefs();
	}
	
	@Override
	public MetaModel build() {
		MetaClass target = MetaClass.forName(packageName, simpleName);
		target.setModifiers(std.mod_public);
		target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
		target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
		target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxJwsWebService, target));
		
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
