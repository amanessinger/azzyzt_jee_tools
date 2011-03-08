package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class InvocationRegistryModelBuilder extends DerivedModelBuilder implements Builder {

	public InvocationRegistryModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "meta");
			
			// upon first entity create MetaClass
			String simpleName = "InvocationRegistry";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setSuperMetaClass(std.InvocationRegistryBase);
			targetModel.follow(packageName);
			target.setModifiers(std.mod_public);
			
			MetaAnnotationInstance mai;
			mai = new MetaAnnotationInstance(std.javaxEjbLocalBean, target);
			target.addMetaAnnotationInstance(mai);
			mai = new MetaAnnotationInstance(std.javaxEjbStateless, target);
			target.addMetaAnnotationInstance(mai);
			mai = new MetaAnnotationInstance(std.javaxInterceptorInterceptor, target);
			target.addMetaAnnotationInstance(mai);
			
			target.addReferencedForeignType(std.javaxEjbEJB);
			target.addReferencedForeignType(std.javaxAnnotationResource);
			target.addReferencedForeignType(std.javaxTransactionTransactionSynchronizationRegistry);
			target.addReferencedForeignType(std.siteAdapterInterface);
			
			// TODO this implies order. We have to make sure that we call modifying builders in the right order. Dependencies?
			masterModel.setProperty("invocation_registry", target);
			
			// now break out
			break;
		}

		return targetModel;
	}
}
