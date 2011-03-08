package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class RESTServletModelBuilder extends DerivedModelBuilder implements Builder {

	public RESTServletModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "service");
			
			// upon first entity create the REST servlet 
			String simpleName = "RESTServlet";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.javaxWsRsCoreApplication);
			MetaAnnotationInstance applicationPath = new MetaAnnotationInstance(std.javaxWsRsApplicationPath, target);
			applicationPath.setElement("value", "REST");
			target.addMetaAnnotationInstance(applicationPath);
			
			// now break out
			break;
		}
		
		return targetModel;
	}
}
