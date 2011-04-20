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

public class RESTStoreMultiModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_NAME = "StoreMultiDelegator";

	public RESTStoreMultiModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dtoBase = (MetaClass) masterModel.getProperty(ModelProperties.DTO_BASE);
			MetaClass svcBean = (MetaClass) masterModel.getProperty(ModelProperties.STORE_MULTI_BEAN);
			MetaClass restInterceptor = (MetaClass) masterModel.getProperty(ModelProperties.REST_INTERCEPTOR);

			// create MetaClass
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			String simpleName = CLASS_NAME;
			String pathString = "storeMulti";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			masterModel.setProperty(ModelProperties.REST_STORE_MULTI_DELEGATOR, target);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.restDelegatorBase);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
			MetaAnnotationInstance path = new MetaAnnotationInstance(std.javaxWsRsPath, target);
			path.setElement("value", pathString);
			target.addMetaAnnotationInstance(path);
			
			target.addReferencedForeignType(dtoBase);
			target.addReferencedForeignType(restInterceptor);
			target.addReferencedForeignType(std.javaxInterceptorInterceptors);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.invalidArgumentException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.javaxWsRsPOST);
			target.addReferencedForeignType(std.javaxWsRsProduces);
			target.addReferencedForeignType(std.javaxWsRsConsumes);
			target.addReferencedForeignType(std.javaxWsRsCoreMediaType);
			target.addReferencedForeignType(std.stringListWrapper);

			addStoreMultiBeanField(target);
			
			target.setProperty(ModelProperties.SVC_BEAN, svcBean);
			target.setProperty(ModelProperties.DTO_BASE, dtoBase);
			
			// now break out
			break;
		}
		
		
		return targetModel;
	}
}
