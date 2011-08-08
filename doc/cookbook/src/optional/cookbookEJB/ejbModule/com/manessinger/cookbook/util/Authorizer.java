package com.manessinger.cookbook.util;

import java.lang.reflect.Method;

import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.meta.Credentials;
import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;
import org.azzyzt.jee.runtime.util.AuthorizationInterface;

public class Authorizer implements AuthorizationInterface {

	@Override
	public void checkAuthorization(
			Class<?> clazz, 
			Method method,
			InvocationMetaInfo mi
	) throws AccessDeniedException 
	{
		Credentials credentials = mi.getCredentials();
		
		if (!credentials.hasCredential("modify")) {
			throw new AccessDeniedException();
		}
	}

}
