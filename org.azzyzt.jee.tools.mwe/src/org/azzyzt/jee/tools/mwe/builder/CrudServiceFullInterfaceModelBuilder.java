package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;

public class CrudServiceFullInterfaceModelBuilder extends DerivedModelBuilder implements Builder {

	public CrudServiceFullInterfaceModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty("dto");

			// create MetaInterface
			String packageName = derivePackageNameFromEntity(me, "service");
			String simpleName = me.getSimpleName();
			simpleName += "FullInterface";
			MetaInterface target = MetaInterface.forName(packageName, simpleName);
			me.setProperty("svcFullInterface", target);
			target.setModifiers(std.mod_public);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbRemote, target));
			
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.invalidFieldException);
			target.addReferencedForeignType(std.querySyntaxException);
			target.addReferencedForeignType(std.notYetImplementedException);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.querySpec);
			
			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			target.setProperty("entity", me);
			target.setProperty("dto", dto);
		}
		return targetModel;
	}
}
