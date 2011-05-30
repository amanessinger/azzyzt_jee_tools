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

package org.azzyzt.jee.tools.common;

import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class Util {
	
	protected static final long POLL_FEEDBACK_INTERVAL_MILLIS = 100;

	public static void callExternalMainClass(
			String jobTitle,
			final URL[] classPathEntries, 
			final String fqMainClassName,
			final String[] args
	) 
	throws InterruptedException 
	{
		Job job = new Job(jobTitle) {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					ConcurrentLinkedQueue<String> log = new ConcurrentLinkedQueue<String>();
					MainRunner r = new MainRunner(classPathEntries, fqMainClassName, args, log);
					Thread t = new Thread(r);
					t.start();
					while (t.getState() != Thread.State.TERMINATED) {
						Thread.sleep(POLL_FEEDBACK_INTERVAL_MILLIS);
						reportQueuedMessages(monitor, log);
					}
					reportQueuedMessages(monitor, log);
					
				} catch (Exception e) {
					return new Status(IStatus.ERROR, Common.PLUGIN_ID, "Call to "+fqMainClassName+" failed", e);
				}

				return Status.OK_STATUS;
			}

			protected void reportQueuedMessages(
					IProgressMonitor monitor,
					ConcurrentLinkedQueue<String> log) 
			{
				String msg;
				while ((msg = log.poll()) != null) {
					monitor.worked(1);
					monitor.subTask(msg);
					Common.getDefault().log(msg);
				}
			}
			
		};
		job.setUser(false);
		job.schedule();
		job.join();
	}

	public static Status createErrorStatus(String msg) {
		return new Status(
				IStatus.ERROR, 
				Common.PLUGIN_ID, 
				msg
				);
	}

	public static CoreException createCoreException(String msg, Throwable cause) {
		CoreException e = new CoreException(createErrorStatus(msg));
		try {
			e.initCause(cause);
		} catch (IllegalArgumentException ex) {
			// What can we do but accept and ignore?
		}
		return e;
	}

}
