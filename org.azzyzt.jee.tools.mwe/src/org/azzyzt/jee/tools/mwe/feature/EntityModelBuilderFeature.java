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

package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.builder.EntityModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public class EntityModelBuilderFeature extends ModelBuilderFeature {
	
	public static final String PROJECT_BASE_NAME = "Project Base Name";
	public static final String TARGET_ENUMERATOR = "Target Enumerator";

	private Log logger;

	public EntityModelBuilderFeature(Log logger) {
		this.logger = logger;
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(PROJECT_BASE_NAME, ParameterType.String, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(TARGET_ENUMERATOR, ParameterType.TargetEnumerator, Parameter.IS_MANDATORY));
		return parameters;
	}
	
	@Override
	public MetaModel build(Parameters parameters) {
		String projectBaseName;
		TargetEnumerator targetEnumerator;
		
		projectBaseName = (String)parameters.byName(PROJECT_BASE_NAME).getValue();
		targetEnumerator = (TargetEnumerator)parameters.byName(TARGET_ENUMERATOR).getValue();

        EntityModelBuilder emb = new EntityModelBuilder(projectBaseName, targetEnumerator, logger);
		MetaModel entityModel = emb.build();
		return entityModel;
	}

}
