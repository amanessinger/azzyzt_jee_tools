package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class RESTExceptionMapperModelBuilder extends DerivedModelBuilder implements Builder {

	public RESTExceptionMapperModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "service");
			
			// upon first entity create the REST exception mapper 
			String simpleName = "RESTExceptionMapper";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.exceptionToSuccessMapper);
			target.addInterface(std.javaxWsRsExtExceptionMapperThrowable);
			MetaAnnotationInstance provider = new MetaAnnotationInstance(std.javaxWsRsCoreProvider, target);
			target.addMetaAnnotationInstance(provider);
			MetaAnnotationInstance xmlRootElement = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, target);
			xmlRootElement.setElement("name", "error");
			target.addMetaAnnotationInstance(xmlRootElement);
			
			// now break out
			break;
		}
		
		return targetModel;
	}
}
