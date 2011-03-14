package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;

public class CrudServiceRestrictedBeansModelBuilder extends DerivedModelBuilder implements Builder {

	public CrudServiceRestrictedBeansModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass entityMetaInfo = (MetaClass)masterModel.getProperty("entityMetaInfo");
				
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty("dto");

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "service");
			String simpleName = me.getSimpleName();
			simpleName += "RestrictedBean";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.addInterface((MetaInterface)me.getProperty("svcRestrictedInterface"));
			me.setProperty("svcRestrictedBean", target);
			target.setModifiers(std.mod_public);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxJwsWebService, target));
			
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(me);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.invalidFieldException);
			target.addReferencedForeignType(std.querySyntaxException);
			target.addReferencedForeignType(std.notYetImplementedException);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.javaUtilArrayList);
			target.addReferencedForeignType(entityMetaInfo);
			target.addReferencedForeignType(std.querySpec);

			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			addGenericEaoField(target);
			addConverterField(target, me);
			addEntityMetaInfoField(target);
			
			target.setProperty("entity", me);
			target.setProperty("dto", dto);
		}
		return targetModel;
	}
}
