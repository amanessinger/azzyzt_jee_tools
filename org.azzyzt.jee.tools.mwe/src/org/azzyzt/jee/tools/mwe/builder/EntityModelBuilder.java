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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public class EntityModelBuilder {

	private TargetEnumerator enumerator;
	private MetaModel entityModel;
	private Log logger;
	

    public EntityModelBuilder(MetaModel model, TargetEnumerator enumerator) {
    	this.enumerator = enumerator;
    	this.entityModel = model;
    	logger = model.getLogger();
    }

    public MetaModel build() {
        entityModel.excludeMethodsFromModel();
        entityModel.excludeStaticFieldsFromModel();
        for (String targetPackage : enumerator.getTargetPackageNames()) {
        	entityModel.follow(targetPackage);
        }
        List<String> entityNames = enumerator.getFullyQualifiedTargetNames();
        entityModel.build(entityNames);
        
        for (MetaClass mc : entityModel.getEmbeddables()) {
        	validate(mc);
        }
        
        for (MetaClass mc : entityModel.getTargetEntities()) {
        	validate(mc);
        }
        
        return entityModel;
    }

	private void validate(MetaClass mc) {
		
		Class<?> ownerClazz = mc.getClazz();
		if (ownerClazz == null) {
			String msg = "MetaClass "+mc.getFqName()+" was synthesized, can't validate class object";
			logger.error(msg);
			throw new ToolError(msg);
		}
		Method[] methods = ownerClazz.getMethods();
		Set<String> methodNames = new HashSet<String>();
		for (Method m : methods) {
			methodNames.add(m.getName());
		}

		List<MetaField> fields = mc.getFields();
		for (MetaField f : fields) {
			
			if (f.isInternal()) continue;
			
			
			String baseName = StringUtils.ucFirst(f.getName());
			String[] getterSetterName = { "get"+baseName, "set"+baseName };

			for (String name : getterSetterName) {
				if (!methodNames.contains(name)) {
					String msg = "Validation error: "+mc.getFqName()+" does not have a method "+name+"()";
					logger.error(msg);
					throw new ToolError(msg);
				}
			}
		}
	}

}
