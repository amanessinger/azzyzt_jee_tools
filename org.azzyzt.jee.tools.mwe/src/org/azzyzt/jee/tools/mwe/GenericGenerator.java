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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.builder.GenericBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;
import org.azzyzt.jee.tools.mwe.util.Log.Verbosity;

public class GenericGenerator {

	public static void main(String[] args) 
	{
        doWork(args, new StreamLog());
    }

	public static void sideEntrance(
			String[] args, 
			ConcurrentLinkedQueue<String> log) 
	{
		doWork(args, new QueueLog(log));
	}

	private static void doWork(String[] args, Log logger)
	{
		try {
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
			if (arguments.size() != 3) {
				logger.info("usage: GenericGenerator <absolute-sourcefolder> <package-name> <builder-class>");
	            return;
	        }
	        String sourceFolder = arguments.get(0);
	        String packageName = arguments.get(1);
	        String builderClazzName = arguments.get(2);
	        
	        Class<?> builderClazz = Class.forName(builderClazzName);
	        Constructor<?> constructor = builderClazz.getConstructor(String.class, Log.class);
	        Object o = constructor.newInstance(packageName, logger);
	        if (!(o instanceof GenericBuilder)) {
	        	logger.info("Class "+builderClazzName+" is not a GenericBuilder");
	            return;
	        }
	        GenericBuilder b = (GenericBuilder)o;
	        
	        MetaModel targetModel = b.build();
	        JavaGenerator targetGen = new JavaGenerator(
	        		targetModel, sourceFolder, b.getTemplateGroup());
			targetGen.setGenerateFields(b.getGenerateFields());
			targetGen.setGenerateDefaultConstructor(b.getGenerateDefaultConstructor());
			targetGen.setGenerateGettersSetters(b.getGenerateGettersSetters());
			int numberGenerated = targetGen.generate();
	        
			logger.info("Generated "+numberGenerated+" sources");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(""+e.getMessage());
		}
	}

}
