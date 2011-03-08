package org.azzyzt.jee.runtime.meta;

import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;

import org.azzyzt.jee.runtime.util.SiteAdapterInterface;

public abstract class InvocationRegistryBase {

	public abstract SiteAdapterInterface getSiteAdapter();
	public abstract TransactionSynchronizationRegistry getTsr();

	public void registerRESTInvocation(InvocationContext ctx) {
		
		SiteAdapterInterface siteAdapter = getSiteAdapter();
		if (siteAdapter != null) {
			InvocationMetaInfo metaInfo = siteAdapter.fromRESTContext(ctx);
			getTsr().putResource("invocationMetaInfo", metaInfo);
		}
	}
	
	public InvocationMetaInfo getMetaInfo() {
		// TODO make sure that the caller (generated) can cope with nulls
		InvocationMetaInfo metaInfo = (InvocationMetaInfo)getTsr().getResource("invocationMetaInfo");
		return metaInfo;
	}

}
