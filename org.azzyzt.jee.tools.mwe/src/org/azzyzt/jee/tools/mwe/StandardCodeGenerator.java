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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;
import org.azzyzt.jee.tools.mwe.builder.AzzyztantBeanBuilder;
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
import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.Log.Verbosity;
import org.azzyzt.jee.tools.mwe.util.ProjectTools;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

public class StandardCodeGenerator {
	
	/*
	 * When loading Azzyztant, it may be that it has just been created by PrerequisiteGenerator.
	 * There will have been a refresh of the project afterwards, but the compiler may not yet
	 * be done compiling. Thus we try loading until we succeed or this number of seconds is up.
	 */
	private static int MAX_NUMBER_OF_SECONDS_TO_WAIT_FOR_COMPILATION_OF_AZZYZTANT = 30;

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
        String ejbSourceFolder = projectPathPrefix+"EJB/generated";
        String ejbClientSourceFolder = projectPathPrefix+"EJBClient/generated";
        String restSourceFolder = projectPathPrefix+"Servlets/generated";

        Parameters parameters;
        int numberOfSourcesGenerated;

        TargetEnumerator enumerator;
        if (3 == arguments.size()) {
            String persistenceUnitName = arguments.get(1);
			enumerator = new EntityEnumerator(persistenceUnitName, logger);
        } else {
            enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD, logger);
        }
        
        MetaModel masterModel = MetaModel.createMasterModel(projectBaseName, logger);
        
        String packagePrefix = ProjectTools.determinePackagePrefix(enumerator, logger);
        
		MetaClass azzyztant = loadAzzyztant(masterModel, packagePrefix, logger);
        GeneratorOptions go = analyzeAzzyztant(azzyztant);
        masterModel.setGeneratorOptions(go);
        
        EntityModelBuilderFeature embf = new EntityModelBuilderFeature(masterModel, logger);
        
		parameters = embf.getParameters();
        parameters.byName(EntityModelBuilderFeature.TARGET_ENUMERATOR).setValue(enumerator);
		embf.build(parameters);

        SingleTargetsGeneratorFeature singleTargetsGen = new SingleTargetsGeneratorFeature(masterModel);
        parameters = singleTargetsGen.getParameters();
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbClientSourceFolder);
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbSourceFolder);
		numberOfSourcesGenerated = singleTargetsGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" eao file(s) generated");
		
		DtoGeneratorFeature dtoGen = new DtoGeneratorFeature(masterModel);
        parameters = dtoGen.getParameters();
        parameters.byName(DtoGeneratorFeature.SOURCE_FOLDER).setValue(ejbClientSourceFolder);
        numberOfSourcesGenerated = dtoGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" dto file(s) generated");
        
        EntityDtoConverterGeneratorFeature convGen = new EntityDtoConverterGeneratorFeature(masterModel);
        parameters = convGen.getParameters();
        parameters.byName(EntityDtoConverterGeneratorFeature.SOURCE_FOLDER).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = convGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" converter file(s) generated");
		
		CrudServiceBeansGeneratorFeature svcGen = new CrudServiceBeansGeneratorFeature(masterModel);
		parameters = svcGen.getParameters();
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbClientSourceFolder);
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = svcGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" service beans and service interfaces generated");
		
        if ((masterModel.isGeneratingRestXml() || masterModel.isGeneratingRestJson())) {
			CrudServiceRESTGeneratorFeature restGen = new CrudServiceRESTGeneratorFeature(masterModel);
			parameters = restGen.getParameters();
	        parameters.byName(CrudServiceRESTGeneratorFeature.SOURCE_FOLDER).setValue(restSourceFolder);
	        numberOfSourcesGenerated = restGen.generate(parameters);
	        logger.info(numberOfSourcesGenerated+" REST wrapper file(s) generated");
        }

        ModifyMultiGeneratorFeature smGen = new ModifyMultiGeneratorFeature(masterModel);
        parameters = smGen.getParameters();
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbClientSourceFolder);
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbSourceFolder);
        parameters.byName(ModifyMultiGeneratorFeature.SOURCE_FOLDER_SERVLET_PROJECT).setValue(restSourceFolder);
        numberOfSourcesGenerated = smGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" store multi support file(s) generated");
	}

	private static GeneratorOptions analyzeAzzyztant(MetaClass azzyztant) {
		GeneratorOptions result = new GeneratorOptions();
		MetaStandardDefs std = MetaType.getStandardtypes();
		List<MetaAnnotationInstance> mais = azzyztant.getMetaAnnotationInstances();
		for (MetaAnnotationInstance mai : mais) {
			if (mai.getMetaAnnotation().equals(std.azzyztGeneratorOptions)) {
				AzzyztGeneratorCutback[] cutbacks = 
					(AzzyztGeneratorCutback[])mai.getRawValue("cutbacks");
				for (AzzyztGeneratorCutback c : cutbacks) {
					result.addCutback(c);
				}
			}
		}
		return result;
	}

	private static MetaClass loadAzzyztant(
			MetaModel masterModel,
			String packagePrefix, 
			Log logger
	) {
        String metaPackagePrefix = packagePrefix+"."+PackageTails.META;
		String fqAzzyztantName = metaPackagePrefix+"."+AzzyztantBeanBuilder.AZZYZTANT_BEAN_NAME;
		
		long start = System.currentTimeMillis();
		do {
	        try {
				Class<?> azzyztantClazz = Class.forName(fqAzzyztantName);
				MetaClass metaAzzyztant = MetaClass.forType(azzyztantClazz);
				masterModel.follow(metaPackagePrefix);
				masterModel.addMetaDeclaredTypeIfTarget(metaAzzyztant);
				masterModel.setProperty(ModelProperties.AZZYZTANT, metaAzzyztant);
				return metaAzzyztant;
			} catch (ClassNotFoundException e) {
				try {
					logger.info("Generated class "+fqAzzyztantName+" not yet compiled, trying to load again in a second ...");
					Thread.sleep(1000);
				} catch (InterruptedException e1) { }
			}
		} while (System.currentTimeMillis() - start < MAX_NUMBER_OF_SECONDS_TO_WAIT_FOR_COMPILATION_OF_AZZYZTANT * 1000);
		
		String msg = "Generated class "+fqAzzyztantName+" can't be loaded, giving up";
		logger.info(msg);
		throw new ToolError(msg);
	}

}
