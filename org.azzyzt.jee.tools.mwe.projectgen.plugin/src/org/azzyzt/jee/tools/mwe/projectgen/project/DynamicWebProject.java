package org.azzyzt.jee.tools.mwe.projectgen.project;

import org.eclipse.core.runtime.CoreException;

public class DynamicWebProject extends JavaProject {

	public static DynamicWebProject create(String name, Context context, JavaProject...projectsOnBuildPath) 
	throws CoreException 
	{
		DynamicWebProject dyn = new DynamicWebProject(name, context);
		
		dyn.addProjectsToBuildPath(projectsOnBuildPath);
		
		return dyn;
	}

	public DynamicWebProject(String name, Context context, String...sourceFolderNames) 
	throws CoreException 
	{
		super(name, context, sourceFolderNames);
	}
	
}
