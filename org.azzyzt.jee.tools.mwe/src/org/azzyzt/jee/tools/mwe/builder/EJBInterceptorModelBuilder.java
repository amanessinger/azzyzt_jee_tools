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

public class EJBInterceptorModelBuilder extends DerivedModelBuilder implements Builder {

	private static final String CLASS_NAME = "EJBInterceptor";

	public EJBInterceptorModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntityAndFollowPackage(me, PackageTails.META);
			
			// upon first entity create MetaClass
			String simpleName = CLASS_NAME;
			MetaClass target = MetaClass.forName(packageName, simpleName);
			targetModel.follow(packageName);
			target.setModifiers(std.mod_public);
			
			MetaAnnotationInstance mai;
			mai = new MetaAnnotationInstance(std.javaxEjbStateless, target);
			target.addMetaAnnotationInstance(mai);
			mai = new MetaAnnotationInstance(std.javaxInterceptorInterceptor, target);
			target.addMetaAnnotationInstance(mai);
			
			target.addReferencedForeignType(std.javaxEjbEJB);
			target.addReferencedForeignType(std.javaxInterceptorAroundInvoke);
			target.addReferencedForeignType(std.javaxInterceptorInvocationContext);
			target.addReferencedForeignType(std.javaxEjbEJBTransactionRolledbackException);
			target.addReferencedForeignType(std.translatableException);
			target.addReferencedForeignType(std.javaxXmlWsWebServiceContext);
			target.addReferencedForeignType(std.invocationMetaInfo);
			target.addReferencedForeignType(std.authorizationInterface);

			addGenericEaoField(target);
			addEJBSessionContext(target);
			addInvocationRegistryField(target);
			
			masterModel.setProperty(ModelProperties.TRANSACTION_ROLLBACK_HANDLER, target);
			
			// now break out
			break;
		}

		return targetModel;
	}
}
