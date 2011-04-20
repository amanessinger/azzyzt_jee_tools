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

import java.util.List;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public class EntityModelBuilder {

	private Log logger;
	private TargetEnumerator enumerator;

    public EntityModelBuilder(Log logger) {
    	this.logger = logger;
        enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD);
    }

    public EntityModelBuilder(String persistenceUnitName, Log logger) {
    	this.logger = logger;
        enumerator = new EntityEnumerator(persistenceUnitName);
    }

    public MetaModel build() {
        MetaModel entityModel = new MetaModel(this.getClass().getSimpleName(), logger);
        entityModel.excludeMethodsFromModel();
        entityModel.excludeStaticFieldsFromModel();
        for (String targetPackage : enumerator.getTargetPackageNames()) {
        	entityModel.follow(targetPackage);
        }
        List<String> entityNames = enumerator.getFullyQualifiedTargetNames();
        entityModel.build(entityNames);        
        return entityModel;
    }

}
