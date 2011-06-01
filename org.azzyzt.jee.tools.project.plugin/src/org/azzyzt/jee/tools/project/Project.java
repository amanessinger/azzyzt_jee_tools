package org.azzyzt.jee.tools.project;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class Project {

	private String name;
	protected Context context;
	protected IProject p;

	public Project() {
		super();
	}

	public Project(String name, Context context) 
	throws CoreException 
	{
		super();
		this.context = context;
		
		p = context.getRoot().getProject(name);

		if (!p.exists()) {
			IProjectDescription dsc = context.getWorkspace().newProjectDescription(name);
			p.create(dsc, context.getSubMonitor());
		}
		p.open(context.getSubMonitor());
	}

	protected void addMarkerNature(String nature) 
	throws CoreException 
	{
		if (p.hasNature(nature)) return;
		
		IProjectDescription projectDescription = p.getDescription();
		String[] natureIds = projectDescription.getNatureIds();
		String[] newNatureIds = new String[natureIds.length + 1];
		System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
		newNatureIds[0] = nature;
		projectDescription.setNatureIds(newNatureIds);
		p.setDescription(projectDescription, context.getSubMonitor());
	}

	protected Path createFolderPathIfNeeded(String folderName)
	throws CoreException 
	{
		Path path = new Path(folderName);
		createFolderForPathIfNeeded(path);
		return path;
	}

	protected IFolder createFolderForPathIfNeeded(Path path)
	throws CoreException 
	{
		IFolder f = p.getFolder(path);
		if (!f.exists()) {
			f.create(true, true, context.getSubMonitor());
		}
		return f;
	}

	protected void copyFromUrlToFolder(IContainer iContainer, URL content, String fileName)
	throws IOException, CoreException 
	{
		InputStream in = content.openConnection().getInputStream();
		iContainer.getFile(new Path(fileName)).create(in, true, getContext().getSubMonitor());
	}

	protected void refresh() 
	throws CoreException 
	{
		p.refreshLocal(IResource.DEPTH_INFINITE, context.getSubMonitor());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IProject getP() {
		return p;
	}

	public void setP(IProject p) {
		this.p = p;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

}