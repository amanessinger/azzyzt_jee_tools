package org.azzyzt.jee.runtime.meta;

import java.util.Calendar;

import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;

import org.azzyzt.jee.runtime.util.SiteAdapterInterface;

public interface InvocationRegistryInterface {

	public SiteAdapterInterface getSiteAdapter();

	public TransactionSynchronizationRegistry getTsr();
	
	public Calendar getInvocationTimestamp();

	public AzzyztantInterface getAzzyztant();
	
	public InvocationMetaInfo registerRESTInvocation(InvocationContext ctx);
	
	public InvocationMetaInfo registerEJBInvocation(InvocationContext ctx);

}