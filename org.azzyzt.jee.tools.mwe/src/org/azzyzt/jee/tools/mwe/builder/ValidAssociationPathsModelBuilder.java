package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class ValidAssociationPathsModelBuilder extends DerivedModelBuilder implements Builder {

	public ValidAssociationPathsModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "meta");
			
			// upon first entity create the REST servlet 
			String simpleName = "ValidAssociationPaths";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.validAssociationPathsBase);
			target.addInterface(std.validAssociationPathsInterface);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbSingleton, target));

			target.addReferencedForeignType(std.associationPathInfo);
			target.addReferencedForeignType(std.javaxEjbLock);
			target.addReferencedForeignType(std.javaxEjbLockType);
			
			masterModel.setProperty("validAssociationPaths", target);

			// now break out
			break;
		}
		
		return targetModel;
	}
}
