package com.manessinger.cookbook.meta;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.azzyzt.jee.runtime.annotation.AzzyztGeneratorOptions;
import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;
import org.azzyzt.jee.runtime.meta.AzzyztGeneratorOption;
import org.azzyzt.jee.runtime.meta.AzzyztantInterface;
import org.azzyzt.jee.runtime.util.AuthorizationInterface;
import org.azzyzt.jee.runtime.util.CredentialBasedAuthorizer;
import org.azzyzt.jee.runtime.util.StringConverterInterface;


/**
 * Generated class com.manessinger.cookbook.meta.Azzyztant
 * 
 * This class is only generated if it does not exist. It is intended to be 
 * modified. 
 */
@LocalBean
@Stateless
@AzzyztGeneratorOptions(
        cutbacks = {
        		// AzzyztGeneratorCutback.NoRestServicesJson, 
        		// AzzyztGeneratorCutback.NoRestServicesXml,
                AzzyztGeneratorCutback.NoRemoteInterfaces,
                //AzzyztGeneratorCutback.NoSoapServices,
        },
        options = {
        		AzzyztGeneratorOption.AddCxfRestClient,
        		AzzyztGeneratorOption.AddCredentialBasedAuthorization,
        }
)
public class Azzyztant implements AzzyztantInterface {

    /*
     * At runtime, azzyzted projects ask the site adapter for the name of the 
     * user invoking a service. The site adapter is expected to return a name
     * as supplied by a portal in front of the application server, or use any
     * other site-specific means to find out who the user is. The problem is
     * that sometimes user names have special formats (like a Windows domain
     * in front of the actual user name), and some applications may need 
     * another format (e.g. without domain name). Here's your chance to step
     * in between site adapter and runtime library:
     *
     * 'usernameConverter' can be set to an instance of any class that implements
     * StringConverterInterface.
     *
     * ATTENTION: keep this stateless, fast and thread-safe!!! 
     * 
     * This is actually a shared instance that, if not null, is called once 
     * upon any invocation. The runtime won't try to synchronize its call to 
     * 'convert()'. Neither should you.
     */
    private final StringConverterInterface usernameConverter = null;
    
    /*
     * 'authorizer' can be set to an instance of any class that implements
     * AuthorizationInterface.
     *
     * ATTENTION: keep this stateless, fast and thread-safe!!! 
     * 
     * This is actually a shared instance that, if not null, is called once 
     * upon any invocation. The runtime won't try to synchronize its call to 
     * 'checkAuthorization()'. Neither should you.
     */
    private final AuthorizationInterface authorizer = new CredentialBasedAuthorizer();
    
    public Azzyztant() { super(); }


    @Override
    public StringConverterInterface getUsernameConverter() {
        return usernameConverter;
    }

	@Override
	public AuthorizationInterface getAuthorizer() {
		return authorizer;
	}

}