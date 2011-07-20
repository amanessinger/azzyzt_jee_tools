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
import org.azzyzt.jee.tools.mwe.identifiers.RESTPathFragments;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;

public class RESTModifyMultiInterfaceModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_NAME = "ModifyMultiCxfRestInterface";

	public RESTModifyMultiInterfaceModelBuilder(MetaModel entityModel, String targetPackageName) {
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
			String pathString = RESTPathFragments.RESTRICTED+"modifyMulti";
			MetaInterface target = MetaInterface.forName(packageName, simpleName);
			masterModel.setProperty(ModelProperties.REST_STORE_MULTI_CXF_INTERFACE, target);
			target.setModifiers(std.mod_public);
			MetaAnnotationInstance path = new MetaAnnotationInstance(std.javaxWsRsPath, target);
			path.setElement("value", pathString);
			target.addMetaAnnotationInstance(path);
			
			target.addReferencedForeignType(dtoBase);
			target.addReferencedForeignType(storeDeleteDto);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.javaxWsRsPOST);
			target.addReferencedForeignType(std.javaxWsRsProduces);
			target.addReferencedForeignType(std.javaxWsRsConsumes);
			target.addReferencedForeignType(std.javaxWsRsCoreMediaType);

			target.setProperty(ModelProperties.DTO_BASE, dtoBase);
			
			// now break out
			break;
		}
		
		
		return targetModel;
	}
}
