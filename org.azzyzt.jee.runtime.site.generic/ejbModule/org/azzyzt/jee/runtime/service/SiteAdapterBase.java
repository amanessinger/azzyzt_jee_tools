package org.azzyzt.jee.runtime.service;

import javax.interceptor.InvocationContext;

import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

public class SiteAdapterBase {

    public SiteAdapterBase() { }

    public InvocationMetaInfo fromRESTContext(InvocationContext ctx) {

    	InvocationMetaInfo i = new InvocationMetaInfo();
    	i.setAuthenticatedUserName("anonymous");
    	
    	return i;
    }
    
}
