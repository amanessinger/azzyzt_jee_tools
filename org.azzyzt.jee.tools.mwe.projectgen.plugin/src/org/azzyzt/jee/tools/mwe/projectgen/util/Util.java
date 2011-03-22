package org.azzyzt.jee.tools.mwe.projectgen.util;

import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;
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
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Call to "+fqMainClassName+" failed", e);
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
					Activator.getDefault().log(msg);
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
				Activator.PLUGIN_ID, 
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
