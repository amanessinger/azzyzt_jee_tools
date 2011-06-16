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

import org.azzyzt.jee.tools.mwe.builder.DtoBaseModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.EaoModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.InvocationRegistryModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.SiteAdapterModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.StoreDeleteDtoModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.TransactionRollbackHandlerModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.TypeMetaInfoModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.ValidAssociationPathsModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class SingleTargetsGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER_EJB_PROJECT = "Source Folder (EJB Project)";
	public static final String SOURCE_FOLDER_CLIENT_PROJECT = "Source Folder (Client Project)";

	public SingleTargetsGeneratorFeature(MetaModel entityModel) {
		super(entityModel);
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
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_EJB_PROJECT).getValue();
		
		MetaModel masterModel = getMasterModel();
		
		MetaModel targetModel;
		JavaGenerator targetGen;
		
		/*
		 * TODO Dependencies of features and builders: Basically we have one master model 
		 * that we augment by calling different features and features calling builders.
		 * Features may depend on other features having been run before, because builders 
		 * may depend on other builders having been run before. Basically we need a way 
		 * to specify the builders needed in a feature, but the builders would get called
		 * according to their dependencies automatically.
		 * 
		 * Same with features. Features would expose the builders that they are about to 
		 * run and the builders that their builders need to have been run. A top-level
		 * generator would declare needed features, and the features would be sorted 
		 * automatically, according to their needed and provided builders.
		 */
		
		targetModel = new ValidAssociationPathsModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaValidAssociationPathGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(true);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated = targetGen.generate();

		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_CLIENT_PROJECT).getValue();

		targetModel = new DtoBaseModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaDtoBaseGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();

		targetModel = new StoreDeleteDtoModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", masterModel);
		numberOfSourcesGenerated += targetGen.generate();

		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_EJB_PROJECT).getValue();
		
		targetModel = new TypeMetaInfoModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaTypeMetaInfoGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();

		targetModel = new EaoModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGenericEaoGroup", masterModel);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new InvocationRegistryModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaInvocationRegistryGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new SiteAdapterModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaSiteAdapterGroup", masterModel);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();

		targetModel = new TransactionRollbackHandlerModelBuilder(getMasterModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaTransactionRollbackHandlerGroup", masterModel);
		targetGen.setGenerateFields(true);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();

		return numberOfSourcesGenerated;
	}

}
