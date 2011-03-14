package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class EntityMetaInfoModelBuilder extends DerivedModelBuilder implements Builder {

	public EntityMetaInfoModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass target = null;
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			
			for (MetaAssociationEndpoint mae : me.getAssociationEndpoints()) {
				MetaType fieldType = mae.getSourceField().getFieldType();
				if (fieldType.isEntity()) {
					// single-valued
				} else if (fieldType.isEntityCollection()) {
					// multi-valued
				} else {
					throw new ToolError("Field type of MetaAssociationEndpoint is neither entity nor a collection of entities");
				}
			}

			if (target == null) {
				// create MetaClass
				String packageName = derivePackageNameFromEntity(me, "meta");
				
				// upon first entity create the EntityMetaInfo class
				String simpleName = "EntityMetaInfo";
				target = MetaClass.forName(packageName, simpleName);
				target.setModifiers(std.mod_public);
				target.setSuperMetaClass(std.entityMetaInfoBase);
				target.addInterface(std.typeMetaInfo);
				target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
				target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
				
				target.addReferencedForeignType(std.typeMetaInfo);
				target.addReferencedForeignType(std.javaUtilSet);
				target.addReferencedForeignType(std.javaUtilHashSet);
				target.addReferencedForeignType(std.javaUtilMap);
				target.addReferencedForeignType(std.javaUtilHashMap);
				target.addReferencedForeignType(std.associationInfo);
				target.addReferencedForeignType(std.requiredSelectionType);
				target.addReferencedForeignType(std.joinType);
				
				addValidAssociationPaths(target);
				target.addReferencedForeignType(std.validAssociationPathsInterface);
				
				masterModel.setProperty("entityMetaInfo", target);
			}
			
			// add a pseudo-field that we can use in the template
			MetaField mf = new MetaField(target, me.getLcFirstSimpleName());
			mf.setFieldType(me);
			target.addField(mf);
		}
		
		return targetModel;
	}
}
