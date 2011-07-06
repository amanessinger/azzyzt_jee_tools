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

import org.azzyzt.jee.tools.mwe.builder.RESTModifyMultiInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTModifyMultiModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.ModifyMultiBeanModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.ModifyMultiInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class ModifyMultiGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER_EJB_PROJECT = "Source Folder (EJB Project)";
	public static final String SOURCE_FOLDER_CLIENT_PROJECT = "Source Folder (Client Project)";
	public static final String SOURCE_FOLDER_SERVLET_PROJECT = "Source Folder (Servlet Project)";
	public static final String CXF_REST_CLIENT_SOURCE_FOLDER = "CXF REST Client Source Folder";

	public ModifyMultiGeneratorFeature(MetaModel entityModel) {
		super(entityModel);
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(SOURCE_FOLDER_EJB_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(SOURCE_FOLDER_CLIENT_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(SOURCE_FOLDER_SERVLET_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(CXF_REST_CLIENT_SOURCE_FOLDER, ParameterType.SourceFolder, Parameter.IS_OPTIONAL));
		return parameters;
	}
	
	@Override
	public int generate(Parameters parameters) {
		int numberOfSourcesGenerated;
		String sourceFolder;
		String cxfRestClientSourceFolder = null;
		
		MetaModel masterModel = getMasterModel();
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_CLIENT_PROJECT).getValue();
		if (masterModel.isGeneratingCxfRestClient()) {
			cxfRestClientSourceFolder = (String)parameters.byName(CXF_REST_CLIENT_SOURCE_FOLDER).getValue();
		}
		
		MetaModel targetModel = new ModifyMultiInterfaceModelBuilder(getMasterModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaModifyMultiGroup", masterModel);
		numberOfSourcesGenerated = targetGen.generate();
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_EJB_PROJECT).getValue();
		
		targetModel = new ModifyMultiBeanModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaModifyMultiGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
				
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_SERVLET_PROJECT).getValue();
		
        if ((masterModel.isGeneratingRestXml() || masterModel.isGeneratingRestJson())) {
        	
			targetModel = new RESTModifyMultiModelBuilder(getMasterModel(), null).build();
			targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTModifyMultiGroup", masterModel);
			targetGen.setGenerateGettersSetters(false);
			numberOfSourcesGenerated += targetGen.generate();
			
			if (masterModel.isGeneratingCxfRestClient()) {
				targetModel = new RESTModifyMultiInterfaceModelBuilder(getMasterModel(), null).build();
				targetGen = new JavaGenerator(targetModel, cxfRestClientSourceFolder, "javaRESTModifyMultiInterfaceGroup", masterModel);
				targetGen.setGenerateGettersSetters(false);
				numberOfSourcesGenerated += targetGen.generate();
			}
        }
				
		return numberOfSourcesGenerated;
	}

}
