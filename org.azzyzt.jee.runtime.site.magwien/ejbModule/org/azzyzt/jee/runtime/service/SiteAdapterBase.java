/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
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

import javax.interceptor.InvocationContext;
import javax.ws.rs.core.HttpHeaders;

import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

public class SiteAdapterBase {

    public SiteAdapterBase() { }

    public InvocationMetaInfo fromRESTContext(InvocationContext ctx) {
    	InvocationMetaInfo i = new InvocationMetaInfo();
    	i.setAuthenticatedUserName("anonymous");

    	RESTDelegatorBase target = (RESTDelegatorBase)ctx.getTarget();
    	
    	if (target == null) return i;
    	
    	HttpHeaders httpHeaders = target.getHttpHeaders();
    	
    	if (httpHeaders == null) return i;
    	
    	List<String> userIds = httpHeaders.getRequestHeader("x-authenticate-userid");
    	
    	if (userIds == null || userIds.isEmpty()) return i;
    	
    	i.setAuthenticatedUserName(userIds.get(0));
    	
    	return i;
    }
    
}
