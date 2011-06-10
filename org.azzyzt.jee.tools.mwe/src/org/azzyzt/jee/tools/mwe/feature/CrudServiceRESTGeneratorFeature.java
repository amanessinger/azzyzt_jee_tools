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

import org.azzyzt.jee.tools.mwe.builder.CrudServiceRESTFullModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRESTRestrictedModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTExceptionMapperModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTInterceptorModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTServletModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class CrudServiceRESTGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER = "Source Folder";

	public CrudServiceRESTGeneratorFeature(MetaModel entityModel) {
		super(entityModel);
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(SOURCE_FOLDER, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		return parameters;
	}
	
	@Override
	public int generate(Parameters parameters) {
		int numberOfSourcesGenerated;
		String sourceFolder;
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER).getValue();
		
		MetaModel masterModel = getMasterModel();
		
		MetaModel targetModel = new RESTInterceptorModelBuilder(getMasterModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTInterceptorGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated = targetGen.generate();
		
		targetModel = new CrudServiceRESTFullModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTFullGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new CrudServiceRESTRestrictedModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTRestrictedGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new RESTServletModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new RESTExceptionMapperModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		return numberOfSourcesGenerated;
	}

}
