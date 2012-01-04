/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.runtime.service;

import java.util.List;
import java.util.Map;

import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.azzyzt.jee.runtime.meta.Credential;
import org.azzyzt.jee.runtime.meta.Credentials;
import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;
import org.azzyzt.jee.runtime.util.SiteAdapterInterface;

public abstract class SiteAdapterBase implements SiteAdapterInterface {
	
	private static final String JNDI_STRINGVALUES_PFX = "custom/stringvalues/";

	private static final String DEFAULT_USERNAME_HEADER = "x-authenticate-userid";
    private static final String JNDI_USERNAME_HEADER = "http/header/username";

    private static final String DEFAULT_CREDENTIALS_HEADER = "x-authorize-roles";
    private static final String JNDI_CREDENTIALS_HEADER = "http/header/credentials";
    
    private static final String CRED_AZZYZT = "azzyzt";
    private static final String CRED_PROP_200_ON_ERROR = "200-on-error";

    private static final String DEFAULT_ANONYMOUS_USER = "anonymous";
    private static final String JNDI_ANONYMOUS_USER = "username/anonymous";
    
    private static String anonymousUser = null;
    private static String usernameHeader = null;
    private static String credentialsHeader = null;
    
	public SiteAdapterBase() { }

	public SiteAdapterBase(String appName) {
		if (anonymousUser == null) {
			anonymousUser = lookupString(
					JNDI_STRINGVALUES_PFX+"app_"+appName+'/'+JNDI_ANONYMOUS_USER, 
					JNDI_STRINGVALUES_PFX+JNDI_ANONYMOUS_USER, 
					DEFAULT_ANONYMOUS_USER);
		}
		if (usernameHeader == null) {
			usernameHeader = lookupString(
					JNDI_STRINGVALUES_PFX+"app_"+appName+'/'+JNDI_USERNAME_HEADER, 
					JNDI_STRINGVALUES_PFX+JNDI_USERNAME_HEADER, 
					DEFAULT_USERNAME_HEADER);
		}
		if (credentialsHeader == null) {
			credentialsHeader = lookupString(
					JNDI_STRINGVALUES_PFX+"app_"+appName+'/'+JNDI_CREDENTIALS_HEADER, 
					JNDI_STRINGVALUES_PFX+JNDI_CREDENTIALS_HEADER, 
					DEFAULT_CREDENTIALS_HEADER);
		}
	}

    public InvocationMetaInfo fromRESTContext(InvocationContext ctx) {
    	
    	InvocationMetaInfo i = new InvocationMetaInfo();
		i.setAuthenticatedUserName(anonymousUser);

    	RESTDelegatorBase target = (RESTDelegatorBase)ctx.getTarget();
    	
    	if (target == null) return i;
    	
    	HttpHeaders httpHeaders = target.getHttpHeaders();
    	
    	if (httpHeaders == null) return i;
    	
    	extractCredentials(i, httpHeaders.getRequestHeader(credentialsHeader));
		extractUserId(i, httpHeaders.getRequestHeader(usernameHeader));
    	
    	return i;
    }

    @SuppressWarnings("unchecked")
	public InvocationMetaInfo fromSOAPContext(WebServiceContext wsc) {
    	
    	InvocationMetaInfo i = new InvocationMetaInfo();
		i.setAuthenticatedUserName(anonymousUser);
		
		if (wsc != null) {
    		MessageContext messageContext = wsc.getMessageContext();
			if (messageContext.containsKey(MessageContext.HTTP_REQUEST_HEADERS)) {
				
    			Map<String, List<String>> httpHeaders;
				httpHeaders = (Map<String, List<String>>)messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
				
		    	extractCredentials(i, httpHeaders.get(credentialsHeader));
				extractUserId(i, httpHeaders.get(usernameHeader));
    		}
		}

    	return i;
    }
    
	private void extractCredentials(InvocationMetaInfo i, List<String> credentialsHeaders) 
	{
		i.setReturn200OnError(false);
		
		String credentials = "";

		if (credentialsHeaders != null && !credentialsHeaders.isEmpty()) {
			// there shouldn't ever be more than one header, but just if, string them together
	    	StringBuffer credentialsSb = new StringBuffer();
	    	for (String h : credentialsHeaders) {
	    		credentialsSb.append(h);
	    		if (!h.endsWith(";")) {
	    			credentialsSb.append(';');
	    		}
	    	}
			credentials = credentialsSb.toString();
		}
    	Credentials creds = Credentials.fromString(credentials); // copes with null/empty/...
		i.setCredentials(creds);
		if (creds.hasCredential(CRED_AZZYZT)) {
			Credential credAzzyzt = creds.getCredential(CRED_AZZYZT);
			if (credAzzyzt.isPropertyTrue(CRED_PROP_200_ON_ERROR)) {
				i.setReturn200OnError(true);
			}
		}
	}
    
	private void extractUserId(InvocationMetaInfo i, List<String> userIds) {
		if (userIds != null && !userIds.isEmpty()) {
			i.setAuthenticatedUserName(userIds.get(0));
		}
	}
	
	private static String lookupString(String appName, String globalName, String defaultValue) {
		String result = null;
		InitialContext ctxt = null;
        try {
			ctxt = new InitialContext();
            result = (String) ctxt.lookup(appName);
        } catch (NamingException e) { }
        if (result == null) {
        	try {
                result = (String) ctxt.lookup(globalName);
        	} catch (NamingException e) { }
        }
        if (result == null) {
        	result = (String) defaultValue;
        }
		return result;
	}
    
}
