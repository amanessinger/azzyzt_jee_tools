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

import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;

public class CrudServiceRestrictedBeansModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_SUFFIX = "RestrictedBean";

	public CrudServiceRestrictedBeansModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass typeMetaInfo = (MetaClass)masterModel.getProperty(ModelProperties.TYPE_META_INFO);
				
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty(ModelProperties.DTO);

			// create MetaClass
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			String simpleName = me.getSimpleName();
			simpleName += CLASS_SUFFIX;
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.addInterface((MetaInterface)me.getProperty(ModelProperties.SVC_RESTRICTED_INTERFACE));
			me.setProperty(ModelProperties.SVC_RESTRICTED_BEAN, target);
			target.setModifiers(std.mod_public);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
			MetaAnnotationInstance mai = new MetaAnnotationInstance(std.javaxJwsWebService, target);
			mai.setElement("serviceName", masterModel.getProjectBaseName());
			target.addMetaAnnotationInstance(mai);
			
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
			target.addReferencedForeignType(typeMetaInfo);
			target.addReferencedForeignType(std.querySpec);

			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			addGenericEaoField(target);
			addConverterField(target, me);
			addTypeMetaInfoField(target);
			
			addTransactionRollbackHandler(target);
			
			target.setProperty(ModelProperties.ENTITY, me);
			target.setProperty(ModelProperties.DTO, dto);
		}
		return targetModel;
	}
}
