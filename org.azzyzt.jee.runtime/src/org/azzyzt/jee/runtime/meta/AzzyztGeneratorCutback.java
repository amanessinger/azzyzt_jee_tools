package org.azzyzt.jee.runtime.meta;

/**
 * AzzyztGeneratorCutback is an enumeration of features that can be 
 * chosen to <strong>not be generated</strong> with Azzyzt JEE Tools. 
 * 
 * @see org.azzyzt.jee.runtime.annotation.AzzyztGeneratorOptions
 */
public enum AzzyztGeneratorCutback {
	
	/**
	 * Do not generate <code>@Remote</code> annotations on generated service bean interfaces.
	 * Use this cutback if you plan to deploy on an application server, that only
	 * implements the JEE 6 web profile. An example is JBoss AS 6.0
	 */
	NoRemoteInterfaces, 
	/**
	 * Do not annotate the generated service beans with <code>@WebService</code>. 
	 * Use this cutback if the service beans should not be available via SOAP.
	 */
	NoSoapServices, 
	/**
	 * Do not generate REST methods with XML parameters and response. If this cutback
	 * is used together with <code>NoRestServicesJson</code>, not even the REST servlet 
	 * is generated and the services can't be reached via REST.
	 */
	NoRestServicesXml, 
	/**
	 * Do not generate REST methods with JSON parameters and response. If this cutback
	 * is used together with <code>NoRestServicesXml</code>, not even the REST servlet 
	 * is generated and the services can't be reached via REST.
	 */
	NoRestServicesJson,

}
