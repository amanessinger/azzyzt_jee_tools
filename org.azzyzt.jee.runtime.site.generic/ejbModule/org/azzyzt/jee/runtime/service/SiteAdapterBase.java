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
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.azzyzt.jee.runtime.meta.Credential;
import org.azzyzt.jee.runtime.meta.Credentials;
import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

public abstract class SiteAdapterBase {

	private static final String DEFAULT_USERNAME_HEADER = "http_x_authenticate_userid";
    private static final String JNDI_USERNAME_HEADER = "custom/stringvalues/http/header/username";

    private static final String DEFAULT_CREDENTIALS_HEADER = "http_x_authorize_roles";
    private static final String JNDI_CREDENTIALS_HEADER = "custom/stringvalues/http/header/roles";
    
    private static final String CRED_AZZYZT = "azzyzt";
    private static final String CRED_PROP_200_ON_ERROR = "200-on-error";

    private static final String DEFAULT_ANONYMOUS_USER = "anonymous";
    private static final String JNDI_ANONYMOUS_USER = "custom/stringvalues/username/anonymous";
    
    private static String anonymousUser;
    private static String usernameHeader;
    private static String credentialsHeader;
    
    static {    	
    	anonymousUser = lookupString(JNDI_ANONYMOUS_USER, DEFAULT_ANONYMOUS_USER);
    	usernameHeader = lookupString(JNDI_USERNAME_HEADER, DEFAULT_USERNAME_HEADER);
    	credentialsHeader = lookupString(JNDI_CREDENTIALS_HEADER, DEFAULT_CREDENTIALS_HEADER);
    }

	public SiteAdapterBase() { }

	protected abstract WebServiceContext getWebServiceContext();

    public InvocationMetaInfo fromRESTContext(InvocationContext ctx) {
    	
    	InvocationMetaInfo i = new InvocationMetaInfo();
		i.setAuthenticatedUserName(anonymousUser);

    	RESTDelegatorBase target = (RESTDelegatorBase)ctx.getTarget();
    	
    	if (target == null) return i;
    	
    	HttpHeaders httpHeaders = target.getHttpHeaders();
    	
    	if (httpHeaders == null) return i;
    	
    	//printAllHeadersToStderr(httpHeaders);
    	
    	List<String> credentialsHeaders = httpHeaders.getRequestHeader(credentialsHeader);
    	if (credentialsHeaders == null) {
    		credentialsHeaders = httpHeaders.getRequestHeader(credentialsHeader.toUpperCase());
    	}
		extractCredentials(i, credentialsHeaders);
		List<String> usernameHeaders = httpHeaders.getRequestHeader(usernameHeader);
		if (usernameHeaders == null) {
			usernameHeaders = httpHeaders.getRequestHeader(usernameHeader.toUpperCase());
		}
		extractUserId(i, usernameHeaders);
    	
    	return i;
    }

    @SuppressWarnings("unchecked")
	public InvocationMetaInfo fromSOAPContext(InvocationContext ctx) {
    	
    	InvocationMetaInfo i = new InvocationMetaInfo();
		i.setAuthenticatedUserName(anonymousUser);
		
		WebServiceContext wsc = getWebServiceContext();
		if (wsc != null) {
    		MessageContext messageContext = wsc.getMessageContext();
			if (messageContext.containsKey(MessageContext.HTTP_REQUEST_HEADERS)) {
				
    			Map<String, List<String>> httpHeaders;
				httpHeaders = (Map<String, List<String>>)messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
				
		    	List<String> credentialsHeaders = httpHeaders.get(credentialsHeader);
		    	if (credentialsHeaders == null) {
		    		credentialsHeaders = httpHeaders.get(credentialsHeader.toUpperCase());
		    	}
				extractCredentials(i, credentialsHeaders);
				
				List<String> usernameHeaders = httpHeaders.get(usernameHeader);
				if (usernameHeaders == null) {
					usernameHeaders = httpHeaders.get(usernameHeader.toUpperCase());
				}
				extractUserId(i, usernameHeaders);
    		}
		}

    	return i;
    }
    
	private void extractCredentials(InvocationMetaInfo i, List<String> credentialsHeaders) 
	{
		i.setReturn200OnError(false);
		
		if (credentialsHeaders == null || credentialsHeaders.isEmpty()) return;
		
		// there shouldn't ever be more than one header, but just if, string them together
    	StringBuffer credentialsSb = new StringBuffer();
    	for (String h : credentialsHeaders) {
    		credentialsSb.append(h);
    		if (!h.endsWith(";")) {
    			credentialsSb.append(';');
    		}
    	}
    	String credentials = credentialsSb.toString();
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
	
	private static String lookupString(String jndiName, String defaultValue) {
		String result;
        try {
            // Lookup via JNDI
            result = (String) new InitialContext()
            .lookup(jndiName);
        } catch (NamingException e) {
            // initialize with default
            result = (String) defaultValue;
        }
		return result;
	}
    
	@SuppressWarnings("unused")
	private void printAllHeadersToStderr(HttpHeaders httpHeaders) {
		MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
    	for (String key : requestHeaders.keySet()) {
    		StringBuffer sb = new StringBuffer();
    		sb.append(key);
    		sb.append(" =");
    		List<String> values = httpHeaders.getRequestHeader(key);
    		for (String value : values) {
    			sb.append(" ");
    			sb.append(value);
    		}
    		System.err.println(sb.toString());
    	}
	}

}
