package org.azzyzt.jee.tools.mwe;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.builder.GenericBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

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
			if (args.length != 3) {
				logger.log("usage: GenericGenerator <absolute-sourcefolder> <package-name> <builder-class>");
	            return;
	        }
	        String sourceFolder = args[0];
	        String packageName = args[1];
	        String builderClazzName = args[2];
	        
	        Class<?> builderClazz = Class.forName(builderClazzName);
	        Constructor<?> constructor = builderClazz.getConstructor(String.class);
	        Object o = constructor.newInstance(packageName);
	        if (!(o instanceof GenericBuilder)) {
	        	logger.log("Class "+builderClazzName+" is not a GenericBuilder");
	            return;
	        }
	        GenericBuilder b = (GenericBuilder)o;
	        
	        MetaModel targetModel = b.build();
	        JavaGenerator targetGen = new JavaGenerator(
	        		targetModel, sourceFolder, b.getTemplateGroup(),
	        		logger);
			targetGen.setGenerateFields(b.getGenerateFields());
			targetGen.setGenerateDefaultConstructor(b.getGenerateDefaultConstructor());
			targetGen.setGenerateGettersSetters(b.getGenerateGettersSetters());
			int numberGenerated = targetGen.generate();
	        
			logger.log("Generated "+numberGenerated+" sources");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(""+e.getMessage());
		}
	}

}
