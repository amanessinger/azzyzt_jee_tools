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

import java.util.List;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaCollection;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntityField;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class EntityDtoConverterModelBuilder extends DerivedModelBuilder implements Builder {

	public EntityDtoConverterModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty("dto");

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "conv");
			String simpleName = me.getSimpleName();
			simpleName += "Conv";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			me.setProperty("conv", target);
			target.setModifiers(std.mod_public);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));

			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(me);
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(std.entityBase);
			
			addGenericEaoField(target);
			if (me.isUsingCreateUserField() || me.isUsingModificationUserField()) {
				addInvocationRegistryField(target);
			}
			
			target.setProperty("entity", me);
			target.setProperty("dto", dto);
			
			List<MetaAssociationEndpoint> maes = me.getAssociationEndpoints();
			for (MetaAssociationEndpoint mae : maes) {
				MetaEntity targetEntity = (MetaEntity)mae.getTargetEntity();
				if (targetEntity.isCombinedId()) {
					target.addReferencedForeignType(targetEntity.getCombinedIdType());
				}
			}
			
			for (MetaField mf : me.getFields()) {
				MetaEntityField mef = (MetaEntityField)mf;
				if (!mef.isInternal()) {
					if (mef.isHoldingAssociationEndpoint()) {
						// we need to import target entities for local variables used in one or the other copy method
				    	MetaDeclaredType targetEntity = mef.getAssociationEndpoint().getTargetEntity();
				    	target.addReferencedForeignType(targetEntity);
				    }
					if (mef.isHoldingMultivaluedAssociationEndpoint()) {
						MetaCollection fieldType = (MetaCollection)mef.getFieldType();
						target.addReferencedForeignType(std.javaUtilList);
						if (fieldType.isList()) {
							target.addReferencedForeignType(std.javaUtilArrayList);
						} else if (fieldType.isSet()) {
							target.addReferencedForeignType(std.javaUtilSet);
							target.addReferencedForeignType(std.javaUtilHashSet);							
						} else if (fieldType.isMap()) {
							target.addReferencedForeignType(std.javaUtilMap);
							target.addReferencedForeignType(std.javaUtilHashMap);							
						}
					}
				}
			}
		}
		return targetModel;
	}
}
