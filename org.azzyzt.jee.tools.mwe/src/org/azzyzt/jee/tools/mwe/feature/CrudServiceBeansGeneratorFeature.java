/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
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

import org.azzyzt.jee.tools.mwe.builder.CrudServiceFullBeansModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceFullInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRestrictedBeansModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRestrictedInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public class CrudServiceBeansGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER_EJB_PROJECT = "Source Folder (EJB Project)";
	public static final String SOURCE_FOLDER_CLIENT_PROJECT = "Source Folder (Client Project)";

	public CrudServiceBeansGeneratorFeature(MetaModel entityModel, Log logger) {
		super(entityModel, logger);
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(SOURCE_FOLDER_EJB_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(SOURCE_FOLDER_CLIENT_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		return parameters;
	}
	
	@Override
	public int generate(Parameters parameters) {
		int numberOfSourcesGenerated;
		String sourceFolder;
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_CLIENT_PROJECT).getValue();
		
		MetaModel targetModel = new CrudServiceFullInterfaceModelBuilder(getModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceFullGroup", logger);
		numberOfSourcesGenerated = targetGen.generate();
		
		targetModel = new CrudServiceRestrictedInterfaceModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceRestrictedGroup", logger);
		numberOfSourcesGenerated += targetGen.generate();
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_EJB_PROJECT).getValue();
		
		targetModel = new CrudServiceFullBeansModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceFullGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new CrudServiceRestrictedBeansModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceRestrictedGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		return numberOfSourcesGenerated;
	}

}
