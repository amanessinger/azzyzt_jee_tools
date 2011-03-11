package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.projectgen.workers.Util;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;

public class JavaProject extends Project {
	
	private IJavaProject jp;

	public static JavaProject create(String name, Context context, String...sourceFolderNames) 
	throws CoreException 
	{
		JavaProject prj = new JavaProject(name, context);
		
		return prj;
	}

	public JavaProject(String name, Context context, String...sourceFolderNames) 
	throws CoreException 
	{
		super(name, context);
		installJavaFacet(sourceFolderNames);
		jp = (IJavaProject)getP().getNature(JavaCore.NATURE_ID);		
	}
	
	protected void installJavaFacet(String...sourceFolderNames)
	throws CoreException 
	{
		JavaFacetInstallConfig config = (JavaFacetInstallConfig) Project.createConfigObject(getContext().getFacets().javaFacetVersion);
		List<IPath> sourceFolderPaths = new ArrayList<IPath>();
		for (String sourceFolderName : sourceFolderNames) {
			sourceFolderPaths.add((IPath) new Path(sourceFolderName));
		}
		config.setSourceFolders(sourceFolderPaths);
		
		installFacet(getContext().getFacets().javaFacetVersion, config);
	}
	
	protected void addProjectsToBuildPath(JavaProject...projectsOnBuildPath) 
	throws CoreException 
	{	
		if (projectsOnBuildPath == null) return;
		
		for (JavaProject p : projectsOnBuildPath) {
			Util.appendProjectToClassPath(jp, p.getJp());
		}
	}

	public void setJp(IJavaProject jp) {
		this.jp = jp;
	}

	public IJavaProject getJp() {
		return jp;
	}
	
}
