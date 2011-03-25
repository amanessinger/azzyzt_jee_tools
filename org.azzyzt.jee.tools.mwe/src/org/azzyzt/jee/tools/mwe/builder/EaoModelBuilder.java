/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

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
