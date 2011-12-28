package org.azzyzt.jee.runtime.util;

import java.lang.reflect.Method;

import org.azzyzt.jee.runtime.annotation.RequiresCredentials;
import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.meta.Credentials;
import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

/**
 * Implements a credential-based authorizer. This can and should be used
 * along with the <code>AzzyztGeneratorOption</code> 
 * <code>AddCredentialBasedAuthorization</code>. For a detailed description 
 * @see org.azzyzt.jee.runtime.meta.Credentials
 */
public class CredentialBasedAuthorizer implements AuthorizationInterface {

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.util.AuthorizationInterface#checkAuthorization(java.lang.Class, java.lang.reflect.Method, org.azzyzt.jee.runtime.meta.InvocationMetaInfo)
	 */
	@Override
	public void checkAuthorization(
			Class<?> clazz, Method method, InvocationMetaInfo mi) 
	throws AccessDeniedException 
	{
		Credentials required = new Credentials();

		RequiresCredentials requiresCredentials = clazz.getAnnotation(RequiresCredentials.class);
		if (requiresCredentials != null) {
			required.mergeFrom(Credentials.fromString(requiresCredentials.value()));
		}
		requiresCredentials = method.getAnnotation(RequiresCredentials.class);
		if (requiresCredentials != null) {
			required.mergeFrom(Credentials.fromString(requiresCredentials.value()));
		}

		Credentials credentials = mi.getCredentials();
		
		if (!credentials.satisfy(required)) {
			throw new AccessDeniedException();
		}
	}

}
