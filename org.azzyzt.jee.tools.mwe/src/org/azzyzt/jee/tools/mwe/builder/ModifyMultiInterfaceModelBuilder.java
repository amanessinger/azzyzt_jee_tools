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

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;
import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;

public class ModifyMultiInterfaceModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_NAME = "ModifyMultiInterface";

	public ModifyMultiInterfaceModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dtoBase = (MetaClass) masterModel.getProperty(ModelProperties.DTO_BASE);
			MetaClass storeDeleteDto = (MetaClass) masterModel.getProperty(ModelProperties.STORE_DELETE_DTO);

			// create MetaInterface
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			String simpleName = CLASS_NAME;
			MetaInterface target = MetaInterface.forName(packageName, simpleName);
			target.setModifiers(std.mod_public);
			if (!masterModel.getGeneratorOptions().hasCutback(AzzyztGeneratorCutback.NoRemoteInterfaces)) {
				target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbRemote, target));
			}
			
			target.addReferencedForeignType(dtoBase);
			target.addReferencedForeignType(storeDeleteDto);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.invalidArgumentException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.duplicateProxyIdException);
			target.addReferencedForeignType(std.invalidProxyIdException);
			target.addReferencedForeignType(std.javaUtilList);
			
			masterModel.setProperty(ModelProperties.MODIFY_MULTI_INTERFACE, target);
			
			// now break out
			break;
		}
		return targetModel;
	}
}
