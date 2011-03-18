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
