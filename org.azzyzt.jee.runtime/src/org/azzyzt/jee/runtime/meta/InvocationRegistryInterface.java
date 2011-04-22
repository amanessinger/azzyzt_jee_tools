package org.azzyzt.jee.runtime.meta;

import javax.transaction.TransactionSynchronizationRegistry;

import org.azzyzt.jee.runtime.util.SiteAdapterInterface;

public interface InvocationRegistryInterface {

	public abstract SiteAdapterInterface getSiteAdapter();

	public abstract TransactionSynchronizationRegistry getTsr();

}