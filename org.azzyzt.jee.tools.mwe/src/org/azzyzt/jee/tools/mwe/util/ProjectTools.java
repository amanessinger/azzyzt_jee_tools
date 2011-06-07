package org.azzyzt.jee.tools.mwe.util;

import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;

public class ProjectTools {

	public static String determinePackagePrefix(TargetEnumerator enumerator, Log logger) {
		/*
	     * TODO Having the package name at hand would definitely help. Passing it into
	     * this generator would mean we have to store it in some project setting.
	     * Otherwise it is available only at project creation time. 
	     * 
	     * Here we rely on the project being azzyzted and the target enumerator having 
	     * a package name that contains a (not necessarily last) part "entity".  
	     */
		String packagePrefix = null;
	    for (String s : enumerator.getTargetPackageNames()) {
	    	int indexOfEntity = s.indexOf(PackageTails.ENTITY, 0);
	    	if (indexOfEntity != -1) {
	    		packagePrefix = s.substring(0, indexOfEntity - 1);
	    		break;
	    	}
	    }
	    if (packagePrefix == null) {
	    	String msg = "Can't determine package prefix!";
	    	logger.error(msg);
			throw new ToolError(msg);
	    }
	    
	    return packagePrefix;
	}

}
