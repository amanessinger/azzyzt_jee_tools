package org.azzyzt.jee.runtime.util;

import javax.interceptor.InvocationContext;

import org.azzyzt.jee.runtime.meta.InvocationMetaInfo;

public interface SiteAdapterInterface {
	
	public InvocationMetaInfo fromRESTContext(InvocationContext ctx);

}
