package org.azzyzt.jee.runtime.meta;

/**
 * AzzyztGeneratorOption is an enumeration of optional features
 * that can be generated with Azzyzt JEE Tools. 
 * 
 * @see org.azzyzt.jee.runtime.annotation.AzzyztGeneratorOptions
 */
public enum AzzyztGeneratorOption {
	
	/**
	 * adds a REST client project using the Apache CXF proxy-based REST client API
	 */
	AddCxfRestClient,
	/**
	 * adds credential based authorization
	 * 
	 * @see org.azzyzt.jee.runtime.meta.Credentials
	 * @see org.azzyzt.jee.runtime.util.CredentialBasedAuthorizer
	 */
	AddCredentialBasedAuthorization,

}
