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

import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class RESTExceptionMapperModelBuilder extends DerivedModelBuilder implements Builder {

	public RESTExceptionMapperModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			
			// upon first entity create the REST exception mapper 
			String simpleName = "RESTExceptionMapper";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.exceptionToSuccessMapper);
			target.addInterface(std.javaxWsRsExtExceptionMapper200Success);
			MetaAnnotationInstance provider = new MetaAnnotationInstance(std.javaxWsRsCoreProvider, target);
			target.addMetaAnnotationInstance(provider);
			MetaAnnotationInstance xmlRootElement = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, target);
			xmlRootElement.setElement("name", "error");
			target.addMetaAnnotationInstance(xmlRootElement);
			target.addReferencedForeignType(std.mapToHttpSuccessException);
			
			// now break out
			break;
		}
		
		return targetModel;
	}
}
