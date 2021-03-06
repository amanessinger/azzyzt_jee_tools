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

public class CrudServiceRESTRestrictedModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_SUFFIX = "RestrictedDelegator";

	public CrudServiceRESTRestrictedModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass dtoBase = (MetaClass) masterModel.getProperty(ModelProperties.DTO_BASE);

		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty(ModelProperties.DTO);
			MetaClass svcBean = (MetaClass) me.getProperty(ModelProperties.SVC_RESTRICTED_BEAN);
			MetaClass restInterceptor = (MetaClass) masterModel.getProperty(ModelProperties.REST_INTERCEPTOR);

			// create MetaClass
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.SERVICE);
			String simpleName = me.getSimpleName();
			String pathString = RESTPathFragments.RESTRICTED+simpleName.toLowerCase();
			simpleName += CLASS_SUFFIX;
			MetaClass target = MetaClass.forName(packageName, simpleName);
			me.setProperty(ModelProperties.REST_RESTRICTED_DELEGATOR, target);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.restDelegatorBase);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
			MetaAnnotationInstance path = new MetaAnnotationInstance(std.javaxWsRsPath, target);
			path.setElement("value", pathString);
			target.addMetaAnnotationInstance(path);
			
			target.addReferencedForeignType(dtoBase);
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(restInterceptor);
			target.addReferencedForeignType(std.javaxInterceptorInterceptors);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.invalidFieldException);
			target.addReferencedForeignType(std.notYetImplementedException);
			target.addReferencedForeignType(std.querySyntaxException);
			target.addReferencedForeignType(std.javaxWsRsGET);
			target.addReferencedForeignType(std.javaxWsRsPOST);
			target.addReferencedForeignType(std.javaxWsRsProduces);
			target.addReferencedForeignType(std.javaxWsRsConsumes);
			if (!me.isCombinedId()) {
				target.addReferencedForeignType(std.javaxWsRsQueryParam);
			}
			target.addReferencedForeignType(std.javaxWsRsCoreMediaType);
			target.addReferencedForeignType(std.querySpec);

			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			addRestrictedServiceBeanField(target, me);
			
			target.setProperty(ModelProperties.SVC_BEAN, svcBean);
			target.setProperty(ModelProperties.ENTITY, me);
			target.setProperty(ModelProperties.DTO, dto);
			target.setProperty(ModelProperties.SVC_BEAN_METHOD_SUFFIX, "_r");
		}
		
		
		return targetModel;
	}
}
