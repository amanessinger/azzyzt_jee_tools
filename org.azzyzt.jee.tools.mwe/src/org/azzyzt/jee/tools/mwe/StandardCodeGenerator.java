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

package org.azzyzt.jee.tools.mwe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.azzyzt.jee.tools.mwe.builder.EntityEnumerator;
import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.feature.CrudServiceBeansGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.CrudServiceRESTGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.DtoGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityDtoConverterGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityModelBuilderFeature;
import org.azzyzt.jee.tools.mwe.feature.ModifyMultiGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.Parameters;
import org.azzyzt.jee.tools.mwe.feature.SingleTargetsGeneratorFeature;
import org.azzyzt.jee.tools.mwe.generator.GeneratorOptions;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.Log.Verbosity;
import org.azzyzt.jee.tools.mwe.util.MainHelper;
import org.azzyzt.jee.tools.mwe.util.ProjectTools;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

public class StandardCodeGenerator {
	
	public static void main(String[] args) {
        doWork(args, new StreamLog());
    }

	public static void sideEntrance(
			String[] args, 
			ConcurrentLinkedQueue<String> log) 
	{
		doWork(args, new QueueLog(log));
	}

	protected static void doWork(String[] args, Log logger) {
		List<String> arguments = new ArrayList<String>();
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("--debug")) {
					logger.setVerbosity(Verbosity.DEBUG);
				}
			} else {
				arguments.add(arg);
			}
		}
		if (arguments.size() < 2) {
			logger.error("usage: StandardCodeGenerator <root_path> <project_prefix> [<persistence_unit_name>]");
            return;
        }

		String rootPath = arguments.get(0);
        String projectBaseName = arguments.get(1);
        String projectPathPrefix = rootPath+projectBaseName;
        String ejbGeneratedSourceFolder = projectPathPrefix+"EJB/generated";
        String ejbGeneratedClientSourceFolder = projectPathPrefix+"EJBClient/generated";
        String restGeneratedSourceFolder = projectPathPrefix+"Servlets/generated";
        
        for (String sourceFolder : new String[]{ ejbGeneratedSourceFolder, ejbGeneratedClientSourceFolder, restGeneratedSourceFolder }) {
        	cleanSourceFolder(sourceFolder);
        }

        Parameters parameters;
        int numberOfSourcesGenerated;

        TargetEnumerator enumerator;
        if (3 == arguments.size()) {
            String persistenceUnitName = arguments.get(2);
			enumerator = new EntityEnumerator(persistenceUnitName, logger);
        } else {
            enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD, logger);
        }
        
        MetaModel masterModel = MetaModel.createMasterModel(projectBaseName, logger);
        
        String packagePrefix = ProjectTools.determinePackagePrefix(enumerator, logger);
        
		MetaClass azzyztant = MainHelper.loadAzzyztant(masterModel, packagePrefix, logger);
        GeneratorOptions go = GeneratorOptions.analyzeAzzyztant(azzyztant);
        masterModel.setGeneratorOptions(go);
        
        EntityModelBuilderFeature embf = new EntityModelBuilderFeature(masterModel, logger);
        
		parameters = embf.getParameters();
        parameters.byName(EntityModelBuilderFeature.TARGET_ENUMERATOR).setValue(enumerator);
		embf.build(parameters);

        SingleTargetsGeneratorFeature singleTargetsGen = new SingleTargetsGeneratorFeature(masterModel);
        parameters = singleTargetsGen.getParameters();
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbGeneratedClientSourceFolder);
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbGeneratedSourceFolder);
		numberOfSourcesGenerated = singleTargetsGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" eao file(s) generated");
		
		DtoGeneratorFeature dtoGen = new DtoGeneratorFeature(masterModel);
        parameters = dtoGen.getParameters();
        parameters.byName(DtoGeneratorFeature.SOURCE_FOLDER).setValue(ejbGeneratedClientSourceFolder);
        numberOfSourcesGenerated = dtoGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" dto file(s) generated");
        
        EntityDtoConverterGeneratorFeature convGen = new EntityDtoConverterGeneratorFeature(masterModel);
        parameters = convGen.getParameters();
        parameters.byName(EntityDtoConverterGeneratorFeature.SOURCE_FOLDER).setValue(ejbGeneratedSourceFolder);
        numberOfSourcesGenerated = convGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" converter file(s) generated");
		
		CrudServiceBeansGeneratorFeature svcGen = new CrudServiceBeansGeneratorFeature(masterModel);
		parameters = svcGen.getParameters();
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbGeneratedClientSourceFolder);
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbGeneratedSourceFolder);
        numberOfSourcesGenerated = svcGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" service beans and service interfaces generated");
		
        if ((masterModel.isGeneratingRestXml() || masterModel.isGeneratingRestJson())) {
			CrudServiceRESTGeneratorFeature restGen = new CrudServiceRESTGeneratorFeature(masterModel);
			parameters = restGen.getParameters();
	        parameters.byName(CrudServiceRESTGeneratorFeature.SOURCE_FOLDER).setValue(restGeneratedSourceFolder);
	        numberOfSourcesGenerated = restGen.generate(parameters);
	        logger.info(numberOfSourcesGenerated+" REST wrapper file(s) generated");
        }

        ModifyMultiGeneratorFeature smGen = new ModifyMultiGeneratorFeature(masterModel);
        parameters = smGen.getParameters();
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbGeneratedClientSourceFolder);
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbGeneratedSourceFolder);
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_SERVLET_PROJECT).setValue(restGeneratedSourceFolder);
        numberOfSourcesGenerated = smGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" store multi support file(s) generated");
	}

	private static void cleanSourceFolder(String sourceFolder) {
		File root = new File(sourceFolder);
		try {
			FileUtils.deleteDirectory(root);
		} catch (IOException e) {
			throw new ToolError(e);
		}
		// Hmm ... we need a refresh afterwards anyway
		root.mkdir();
	}

}
