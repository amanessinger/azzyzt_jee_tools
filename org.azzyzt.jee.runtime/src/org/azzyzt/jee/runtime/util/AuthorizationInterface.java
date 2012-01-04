package org.azzyzt.jee.runtime.util;

import java.lang.reflect.Method;

import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

public interface AuthorizationInterface {
	
	public void checkAuthorization(Class<?> clazz, Method method, InvocationMetaInfo mi)
		throws AccessDeniedException;

}
