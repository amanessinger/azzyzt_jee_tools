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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.builder.AzzyztantBeanBuilder;
import org.azzyzt.jee.tools.mwe.builder.EntityEnumerator;
import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.Log.Verbosity;
import org.azzyzt.jee.tools.mwe.util.ProjectTools;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public class PrerequisiteGenerator {

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
			logger.error("usage: PrerequisiteGenerator <root_path> <project_prefix> [<persistence_unit_name>]");
            return;
        }

		String rootPath = arguments.get(0);
        String projectBaseName = arguments.get(1);
        String projectPathPrefix = rootPath+projectBaseName;
        String ejbUserSourceFolder = projectPathPrefix+"EJB/ejbModule";

        TargetEnumerator enumerator;
        if (3 == arguments.size()) {
            String persistenceUnitName = arguments.get(1);
			enumerator = new EntityEnumerator(persistenceUnitName, logger);
        } else {
            enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD, logger);
        }
        
        MetaModel masterModel = MetaModel.createMasterModel(projectBaseName, logger);
        
        String packagePrefix = ProjectTools.determinePackagePrefix(enumerator, logger);
        
		ensureAzzyztant(masterModel, ejbUserSourceFolder, packagePrefix, logger);
	}

	private static void ensureAzzyztant(
			MetaModel masterModel,
			String ejbUserSourceFolder, 
			String packagePrefix, 
			Log logger
	) {
        String metaPackagePrefix = packagePrefix+"."+PackageTails.META;

        int numberOfSourcesGenerated;
		String azzyztantSource = 
        	ejbUserSourceFolder+
        	"/"+
        	StringUtils.packageToPath(metaPackagePrefix)+
        	"/"+
        	AzzyztantBeanBuilder.AZZYZTANT_BEAN_NAME+".java";
		
        File azzyztantSourceFile = new File(azzyztantSource);
        if (!azzyztantSourceFile.exists()) {
        	logger.info(azzyztantSource+" not found, creating it");
        	MetaModel targetModel = new AzzyztantBeanBuilder(masterModel, metaPackagePrefix).build();
        	JavaGenerator targetGen = new JavaGenerator(targetModel, ejbUserSourceFolder, "javaAzzyztantGroup");
        	targetGen.setGenerateFields(false);
        	targetGen.setGenerateDefaultConstructor(true);
        	targetGen.setGenerateGettersSetters(false);
    		numberOfSourcesGenerated = targetGen.generate();
    		logger.info(numberOfSourcesGenerated+" Azzyztant file(s) generated");
        } else {
        	logger.info(azzyztantSource+" found");
        }
	}

}
