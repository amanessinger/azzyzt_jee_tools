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

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;
import org.azzyzt.jee.tools.mwe.util.Log;

public class AzzyztantBeanBuilder extends DerivedModelBuilder implements GenericBuilder {
	
	private static final String TEMPLATE_GROUP = "javaAzzyztantGroup";

	public static final String AZZYZTANT_BEAN_NAME = "Azzyztant";
	
	private Log logger;
	private String projectBaseName;

	public AzzyztantBeanBuilder(String targetPackageName, Log logger) {
		this.targetPackageName = targetPackageName;
		this.logger = logger;
	}
	
	public AzzyztantBeanBuilder(MetaModel masterModel, String targetPackageName) {
		super(masterModel, targetPackageName);
	}
	
	@Override
	public MetaModel build() {
		if (targetModel == null) {
			targetModel = new MetaModel(this.getClass().getSimpleName(), projectBaseName, logger);
			std = new MetaStandardDefs();
		}
		String simpleName = AZZYZTANT_BEAN_NAME;
		MetaClass target = MetaClass.forName(targetPackageName, simpleName);
		target.setModifiers(std.mod_public);
		target.addInterface(std.azzyztantInterface);
		target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbLocalBean, target));
		target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
		
		target.addReferencedForeignType(std.stringConverterInterface);
		target.addReferencedForeignType(std.authorizationInterface);
		
		targetModel.follow(targetPackageName);
		targetModel.addMetaDeclaredTypeIfTarget(target);
		
		return targetModel;
	}

	@Override
	public String getTemplateGroup() {
		return TEMPLATE_GROUP;
	}

	@Override
	public boolean getGenerateFields() {
		return false;
	}

	@Override
	public boolean getGenerateDefaultConstructor() {
		return true;
	}

	@Override
	public boolean getGenerateGettersSetters() {
		return false;
	}

	@Override
	public void setProjectBaseName(String projectBaseName) {
		this.projectBaseName = projectBaseName;
	}

}
