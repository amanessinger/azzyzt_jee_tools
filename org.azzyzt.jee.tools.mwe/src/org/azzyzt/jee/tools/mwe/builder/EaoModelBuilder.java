package org.azzyzt.jee.tools.mwe.builder;

import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class EaoModelBuilder extends DerivedModelBuilder implements Builder {
	
	public EaoModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}
	
	/* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.Builder#build()
	 */
	public MetaModel build() {

		// create MetaClass
		Set<MetaEntity> targetEntities = masterModel.getTargetEntities();
		if (targetEntities.isEmpty()) {
			throw new ToolError("Entity model has no target entities, can't determine target package");
		}
		MetaEntity me = targetEntities.iterator().next();
		String packageName = derivePackageNameFromEntity(me, "eao");
		String simpleName = "GenericEao";
		MetaClass target = MetaClass.forName(packageName, simpleName);
		target.setModifiers(std.mod_public);
		MetaAnnotationInstance mai = new MetaAnnotationInstance(std.javaxEjbLocalBean, target);
		target.addMetaAnnotationInstance(mai);
		mai = new MetaAnnotationInstance(std.javaxEjbStateless, target);
		target.addMetaAnnotationInstance(mai);
		target.setSuperMetaClass(std.eaoBase);
		
		MetaField metaEntityManagerField = new MetaField(target, "em");
		metaEntityManagerField.setModifiers(std.mod_private);
		metaEntityManagerField.setFieldType(std.javaxPersistenceEntityManager);
		mai = new MetaAnnotationInstance(std.javaxPersistencePersistenceContext, metaEntityManagerField);
		metaEntityManagerField.addMetaAnnotationInstance(mai);
		
		// TODO this implies order. We have to make sure that we call modifying builders in the right order. Dependencies?
		masterModel.setProperty("generic_eao", target);
		
		return targetModel;
	}
}
