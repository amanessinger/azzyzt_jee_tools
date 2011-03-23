package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.net.URL;

import org.azzyzt.jee.tools.common.Util;
import org.azzyzt.jee.tools.mwe.projectgen.ProjectGen;
import org.azzyzt.jee.tools.project.Context;
import org.azzyzt.jee.tools.project.ProjectUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

public class MWEGeneratorWorker {

	private IProgressMonitor monitor;

	public void callMWEGenerator(IProject prj) 
	throws InterruptedException, CoreException 
	{
		monitor.beginTask("Generating code", 100);
		
		try {
			/*
			 * TODO fix this to use a plugin setting stored with the project
			 */
			String prjName = prj.getName();
			String ejbSuffix = Context.PROJECT_SUFFIX_EJB;
			if (!prjName.endsWith(ejbSuffix)) {
				throw Util.createCoreException(
						"The project name does not end with \""
						+ejbSuffix+"\", can't determine project stem", 
						null
				);
			}
			String stem = prjName.substring(0, prjName.length() - 3);
			Context context = new Context();
			context.setProjectBaseName(stem);
			
			URL[] classPathEntries = ProjectUtil.classPathURLsForToolMainClass(prj, ProjectGen.extraURLsForToolMainClass());
			String fqMainClassName = "org.azzyzt.jee.tools.mwe.StandardProjectStructureGenerator";
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			String[] args = {
					root.getLocation()+""+IPath.SEPARATOR+stem
			};

			Util.callExternalMainClass("Generate code from entities", classPathEntries, fqMainClassName, args);
			monitor.worked(60);
			
			for (String name : context.allProjectNames()) {
				IProject project = context.getRoot().getProject(name);
				project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 10));
			}
		} finally {
			monitor.done();
		}
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
}
