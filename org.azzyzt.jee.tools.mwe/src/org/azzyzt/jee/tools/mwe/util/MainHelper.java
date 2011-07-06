package org.azzyzt.jee.tools.mwe.util;

import org.azzyzt.jee.tools.mwe.builder.AzzyztantBeanBuilder;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;

public class MainHelper {

	/*
	 * When loading Azzyztant, it may be that it has just been created by PrerequisiteGenerator.
	 * There will have been a refresh of the project afterwards, but the compiler may not yet
	 * be done compiling. Thus we try loading until we succeed or this number of seconds is up.
	 */
	public static int MAX_NUMBER_OF_SECONDS_TO_WAIT_FOR_COMPILATION_OF_AZZYZTANT = 30;
	
	public static MetaClass loadAzzyztant(
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
		} while (System.currentTimeMillis() - start < MainHelper.MAX_NUMBER_OF_SECONDS_TO_WAIT_FOR_COMPILATION_OF_AZZYZTANT * 1000);
		
		String msg = "Generated class "+fqAzzyztantName+" can't be loaded, giving up";
		logger.info(msg);
		throw new ToolError(msg);
	}

}
