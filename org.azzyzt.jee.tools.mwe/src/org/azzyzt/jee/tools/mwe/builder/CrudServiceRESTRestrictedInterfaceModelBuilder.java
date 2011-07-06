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

public class CrudServiceRESTRestrictedInterfaceModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String INTERFACE_SUFFIX = "RestrictedCxfRestInterface";

	public CrudServiceRESTRestrictedInterfaceModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass dtoBase = (MetaClass) masterModel.getProperty(ModelProperties.DTO_BASE);

		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty(ModelProperties.DTO);

			// create MetaInterface
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			String simpleName = me.getSimpleName();
			String pathString = simpleName.toLowerCase()+"Restricted";
			simpleName += INTERFACE_SUFFIX;
			MetaInterface target = MetaInterface.forName(packageName, simpleName);
			me.setProperty(ModelProperties.REST_RESTRICTED_CXF_INTERFACE, target);
			target.setModifiers(std.mod_public);
			MetaAnnotationInstance path = new MetaAnnotationInstance(std.javaxWsRsPath, target);
			path.setElement("value", pathString);
			target.addMetaAnnotationInstance(path);
			
			target.addReferencedForeignType(dtoBase);
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.javaxWsRsGET);
			target.addReferencedForeignType(std.javaxWsRsPOST);
			target.addReferencedForeignType(std.javaxWsRsProduces);
			target.addReferencedForeignType(std.javaxWsRsConsumes);
			if (!me.isCombinedId()) {
				target.addReferencedForeignType(std.javaxWsRsQueryParam);
			}
			target.addReferencedForeignType(std.javaxWsRsCoreMediaType);

			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			target.setProperty(ModelProperties.ENTITY, me);
			target.setProperty(ModelProperties.DTO, dto);
		}
		
		
		return targetModel;
	}
}
