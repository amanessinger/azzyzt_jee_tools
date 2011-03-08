package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class StandardEntityListenerModelBuilder extends DerivedModelBuilder implements Builder {

	public StandardEntityListenerModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass target = null;
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			if (target == null) {
				// create MetaClass
				String packageName = me.getPackageName();
				targetModel.follow(packageName);
				
				// upon first entity create the class 
				String simpleName = "StandardEntityListeners";
				target = MetaClass.forName(packageName, simpleName);
				target.setModifiers(std.mod_public);
				target.setSuperMetaClass(std.entityListenerBase);
				target.addReferencedForeignType(std.javaUtilHashMap);
				target.addReferencedForeignType(std.javaUtilMap);
				target.addReferencedForeignType(std.javaTextSimpleDateFormat);
				target.addReferencedForeignType(std.javaLangReflectMethod);
				target.addReferencedForeignType(std.javaxPersistencePrePersist);
				target.addReferencedForeignType(std.javaxPersistencePreUpdate);
				target.addReferencedForeignType(std.dateFieldType);
			}
			
			// add a pseudo-field that we can use in the template
			MetaField mf = new MetaField(target, me.getLcFirstSimpleName());
			mf.setFieldType(me);
			target.addField(mf);
		}
		
		return targetModel;
	}
}
