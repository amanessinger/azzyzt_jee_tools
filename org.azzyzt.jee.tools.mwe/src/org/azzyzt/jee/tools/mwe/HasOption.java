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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorOption;
import org.azzyzt.jee.tools.mwe.builder.EntityEnumerator;
import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.generator.GeneratorOptions;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.Log.Verbosity;
import org.azzyzt.jee.tools.mwe.util.MainHelper;
import org.azzyzt.jee.tools.mwe.util.ProjectTools;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

public class HasOption {
	
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
		if (arguments.size() < 3) {
			logger.error("usage: HasOption <result_path> <option_name> <project_prefix> [<persistence_unit_name>]");
			for (String arg : args) {
				logger.error("arg ["+arg+"]");
			}
            return;
        }

		/*
		 * There is no way we could deliver a result. We are void, and even if we were not, 
		 * we are called from an asynchronous runner that's void itself. It's ugly, but we
		 * resort to writing a file :)
		 */
		String resultPath = arguments.get(0);
		File result = new File(resultPath);
		File parentDir = result.getParentFile();
		if (!parentDir.isDirectory()) 
		{
			logger.error("invalid result path, no such directory: "+resultPath);
			return;
		}
		if (!result.exists()) {
			logger.error("invalid result path, file does not exist: "+resultPath);
			return;
		}
		
		String optionName = arguments.get(1);
		AzzyztGeneratorOption option = null;
		try {
			option = AzzyztGeneratorOption.valueOf(optionName);
		} catch (IllegalArgumentException e) {
			logger.error("invalid option name "+optionName);
            return;
		}
		
		String projectBaseName = arguments.get(2);
        
        TargetEnumerator enumerator;
        if (4 == arguments.size()) {
            String persistenceUnitName = arguments.get(3);
			enumerator = new EntityEnumerator(persistenceUnitName, logger);
        } else {
            enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD, logger);
        }
        
        MetaModel masterModel = MetaModel.createMasterModel(projectBaseName, logger);
        
        String packagePrefix = ProjectTools.determinePackagePrefix(enumerator, logger);
        
		MetaClass azzyztant = MainHelper.loadAzzyztant(masterModel, packagePrefix, logger);
        GeneratorOptions go = GeneratorOptions.analyzeAzzyztant(azzyztant);
        
        try {
			FileWriter writer = new FileWriter(result);
			if (go.hasOption(option)) {
				writer.append('Y');
			} else {
				writer.append('N');
			}
			writer.close();
		} catch (IOException e) {
			logger.error("Caught exception while writing result to "+resultPath+": "+e.getMessage());
            return;
		}
	}

}
