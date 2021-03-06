/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
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

package org.azzyzt.jee.runtime.meta;

import java.util.Calendar;

import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.xml.ws.WebServiceContext;

import org.azzyzt.jee.runtime.identifiers.TsrKeys;
import org.azzyzt.jee.runtime.util.SiteAdapterInterface;
import org.azzyzt.jee.runtime.util.StringConverterInterface;

public abstract class InvocationRegistryBase implements InvocationRegistryInterface {

	public abstract SiteAdapterInterface getSiteAdapter();
	public abstract TransactionSynchronizationRegistry getTsr();
	public abstract AzzyztantInterface getAzzyztant();
	
	public InvocationMetaInfo registerRESTInvocation(InvocationContext ctx) {
		InvocationMetaInfo metaInfo = new InvocationMetaInfo();
		TransactionSynchronizationRegistry tsr = getTsr();

		ensureInvocationTimestamp(tsr);
		
		SiteAdapterInterface siteAdapter = getSiteAdapter();
		if (siteAdapter != null) {
			metaInfo = siteAdapter.fromRESTContext(ctx);
			StringConverterInterface usernameConverter = getAzzyztant().getUsernameConverter();
			if (usernameConverter != null) {
				// call converter if supplied
				metaInfo.setAuthenticatedUserName(
						usernameConverter.convert(metaInfo.getAuthenticatedUserName())
				);
			}
		}
		tsr.putResource(TsrKeys.INVOCATION_META_INFO, metaInfo);
		return metaInfo;
	}
	
	public InvocationMetaInfo registerEJBInvocation(WebServiceContext ctx) {
		TransactionSynchronizationRegistry tsr = getTsr();

		ensureInvocationTimestamp(tsr);
		
		InvocationMetaInfo metaInfo = getMetaInfo();		
		if (metaInfo == null) {
			// not already done in REST interceptor 
			metaInfo = new InvocationMetaInfo();
			SiteAdapterInterface siteAdapter = getSiteAdapter();
			if (siteAdapter != null) {
				metaInfo = siteAdapter.fromSOAPContext(ctx);
				StringConverterInterface usernameConverter = getAzzyztant().getUsernameConverter();
				if (usernameConverter != null) {
					// call converter if supplied
					metaInfo.setAuthenticatedUserName(
							usernameConverter.convert(metaInfo.getAuthenticatedUserName())
					);
				}
			}
			tsr.putResource(TsrKeys.INVOCATION_META_INFO, metaInfo);
		}
		return metaInfo;
	}
	
	private Calendar ensureInvocationTimestamp(TransactionSynchronizationRegistry tsr) {
		Object resource = tsr.getResource(TsrKeys.INVOCATION_TIMESTAMP);
		if (resource == null) {
			Calendar invocationTimestamp = Calendar.getInstance();
			tsr.putResource(TsrKeys.INVOCATION_TIMESTAMP, invocationTimestamp);
			return invocationTimestamp;
		} else {
			return (Calendar)resource;
		}
	}
	
	public InvocationMetaInfo getMetaInfo() {
		// TODO make sure that the caller (generated) can cope with nulls
		InvocationMetaInfo metaInfo = (InvocationMetaInfo)getTsr().getResource(TsrKeys.INVOCATION_META_INFO);
		return metaInfo;
	}
	
	public Calendar getInvocationTimestamp() {
		TransactionSynchronizationRegistry tsr = getTsr();

		return ensureInvocationTimestamp(tsr);
		
	}

}
