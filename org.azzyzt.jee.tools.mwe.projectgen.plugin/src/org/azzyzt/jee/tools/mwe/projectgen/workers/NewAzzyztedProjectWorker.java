package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.util.ArrayList;
import java.util.Collections;

import org.azzyzt.jee.tools.mwe.projectgen.project.Context;
import org.azzyzt.jee.tools.mwe.projectgen.project.DynamicWebProject;
import org.azzyzt.jee.tools.mwe.projectgen.project.EarProject;
import org.azzyzt.jee.tools.mwe.projectgen.project.EjbProject;
import org.azzyzt.jee.tools.mwe.projectgen.project.JavaProject;
import org.azzyzt.jee.tools.mwe.projectgen.project.Project;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author advman
 *
 */
public class NewAzzyztedProjectWorker {
	
	private final Context context = new Context();

	public NewAzzyztedProjectWorker() { }

	/**
	 * The main workflow method. It creates all required projects and adds them to an EAR project
	 * 
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public void generate() 
	throws InterruptedException, CoreException 
	{
		context.initializeRuntimeSpecificFacets();
		context.setCreateEjbClient(true);
		
		if (!context.isValid()) throw new CoreException(context.getErrorStatus());
		
		context.getMonitor().beginTask("Generating azzyzted project "+context.getEarProjectName(), 100);
		
		try {
			advanceProgress(0, "Create EAR project");
			
			// We crash upon EAR facet creation if the EAR has been created implicitly. Do it now.
			EarProject ear = EarProject.create(context.getEarProjectName(), context, (Project[])null);
			
			advanceProgress(10, "Create EJB project");
			
			EjbProject ejb =
				new EjbProject(
						context.getEjbProjectName(), context, 
						ear, 
						new ArrayList<JavaProject>()
				);
	
			advanceProgress(70, "Creating servlet project");
			
			new DynamicWebProject(
					context.getServletProjectName(), context, 
					ear, 
					Collections.singletonList((JavaProject)ejb)
				);
		} finally {
			context.getMonitor().done();
		}
	}

	private void advanceProgress(int amountFinished, String nowBeginning) 
	throws InterruptedException 
	{
		IProgressMonitor monitor = context.getMonitor();
		if (monitor.isCanceled()) {
			monitor.done();
			throw new InterruptedException();
		}
		monitor.worked(amountFinished);
		monitor.subTask(nowBeginning);
	}
	

	public Context getContext() {
		return context;
	}

}
