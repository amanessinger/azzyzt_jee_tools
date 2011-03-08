package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class SiteAdapterModelBuilder extends DerivedModelBuilder implements Builder {

	public SiteAdapterModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "meta");
			
			// upon first entity create MetaClass
			String simpleName = "SiteAdapter";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setSuperMetaClass(std.siteAdapterBase);
			target.addInterface(std.siteAdapterInterface);
			targetModel.follow(packageName);
			target.setModifiers(std.mod_public);
			
			MetaAnnotationInstance mai;
			mai = new MetaAnnotationInstance(std.javaxEjbLocalBean, target);
			target.addMetaAnnotationInstance(mai);
			mai = new MetaAnnotationInstance(std.javaxEjbStateless, target);
			target.addMetaAnnotationInstance(mai);
			
			target.addReferencedForeignType(std.javaxEjbLocal);
			
			// now break out
			break;
		}

		return targetModel;
	}
}
