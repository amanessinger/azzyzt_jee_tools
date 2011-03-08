package org.azzyzt.jee.runtime.util;

import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.exception.InvalidFieldException;
import org.azzyzt.jee.runtime.meta.ValidAssociactionPathsInterface;

public interface TypeMetaInfo {
	
	public ValidAssociactionPathsInterface getValidPaths();
	
	public boolean isAssociationPath(String name);
	
	public void fieldVerification(Class<?> clazz, String name)
		throws InvalidFieldException, AccessDeniedException;

	public Class<?> getFieldType(Class<?> clazz, String name) 
    	throws InvalidFieldException, AccessDeniedException;
}
