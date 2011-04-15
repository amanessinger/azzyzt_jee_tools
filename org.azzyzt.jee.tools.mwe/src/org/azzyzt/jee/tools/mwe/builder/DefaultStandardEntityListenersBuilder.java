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
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;
import org.azzyzt.jee.tools.mwe.util.Log;

public class DefaultStandardEntityListenersBuilder implements GenericBuilder {
	
	private static final String TEMPLATE_GROUP = "javaDefaultStandardEntityListenersGroup";

	private static final String CLASS_NAME = "StandardEntityListeners";
	
	private String packageName;
	private String simpleName;
	private MetaModel targetModel;
	private MetaStandardDefs std;

	public DefaultStandardEntityListenersBuilder(String packageName, Log logger) {
		this.packageName = packageName;
		this.simpleName = CLASS_NAME;
		this.targetModel = new MetaModel(logger);
		this.std = new MetaStandardDefs();
	}
	
	@Override
	public MetaModel build() {
		MetaClass target = MetaClass.forName(packageName, simpleName);
		target.setModifiers(std.mod_public);
		target.setSuperMetaClass(std.entityListenerBase);
		target.addReferencedForeignType(std.javaUtilHashMap);
		target.addReferencedForeignType(std.javaUtilMap);
		target.addReferencedForeignType(std.javaTextSimpleDateFormat);
		target.addReferencedForeignType(std.javaLangReflectMethod);
		target.addReferencedForeignType(std.javaxPersistencePrePersist);
		target.addReferencedForeignType(std.javaxPersistencePreUpdate);
		target.addReferencedForeignType(std.dateFieldType);
		
		targetModel.follow(packageName);
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
		return false;
	}

	@Override
	public boolean getGenerateGettersSetters() {
		return false;
	}

}
