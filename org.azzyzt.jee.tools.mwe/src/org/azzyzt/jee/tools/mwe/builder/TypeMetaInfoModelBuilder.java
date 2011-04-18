/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
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

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class TypeMetaInfoModelBuilder extends DerivedModelBuilder implements Builder {

	public TypeMetaInfoModelBuilder(MetaModel entityModel, String targetPackageName) {
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
				String packageName = derivePackageNameFromEntityAndFollowPackage(me, "meta");
				
				// upon first entity create the TypeMetaInfo class
				String simpleName = "TypeMetaInfo";
				target = MetaClass.forName(packageName, simpleName);
				target.setModifiers(std.mod_public);
				target.setSuperMetaClass(std.typeMetaInfoBase);
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
				
				masterModel.setProperty("typeMetaInfo", target);
			}
			
			// add a pseudo-field that we can use in the template
			MetaField mf = new MetaField(target, me.getLcFirstSimpleName());
			mf.setFieldType(me);
			target.addField(mf);
		}
		
		return targetModel;
	}
}
