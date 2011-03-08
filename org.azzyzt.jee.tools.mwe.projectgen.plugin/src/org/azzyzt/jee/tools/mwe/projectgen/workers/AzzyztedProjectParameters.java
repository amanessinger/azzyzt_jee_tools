package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

public class AzzyztedProjectParameters {
	
	public static final String PROJECT_SUFFIX_SERVLETS = "Servlets";
	public static final String PROJECT_SUFFIX_EJB_CLIENT = "EJBClient";
	public static final String PROJECT_SUFFIX_EJB = "EJB";
	public static final String PROJECT_SUFFIX_EAR = "EAR";
	
	private String projectBaseName;
	private String earProjectName;
	private String ejbProjectName;
	private String ejbClientProjectName;
	private String servletProjectName;
	private String packageName;
	private Set<IRuntime> targetRuntimes;
	private IRuntime selectedRuntime;
	private IWorkspace workspace;
	private IWorkspaceRoot root;
	
	public AzzyztedProjectParameters() {
		workspace = ResourcesPlugin.getWorkspace();
		root = workspace.getRoot();
	}

	public void setProjectBaseName(String projectBaseName) {
		this.projectBaseName = projectBaseName;
		this.earProjectName = projectBaseName + PROJECT_SUFFIX_EAR;
		this.ejbProjectName = projectBaseName + PROJECT_SUFFIX_EJB;
		this.ejbClientProjectName = projectBaseName + PROJECT_SUFFIX_EJB_CLIENT;
		this.servletProjectName = projectBaseName + PROJECT_SUFFIX_SERVLETS;
	}
	
	public String validate() {
		if (projectBaseName == null || projectBaseName.isEmpty()) {
			return "Project base name must not be empty";
		}
		if (projectBaseName.replace('\\', '/').indexOf('/', 1) > 0) {
			return projectBaseName+" must not contain directory separators";
		}
		if (packageName == null || packageName.isEmpty()) {
			return "Package name must not be empty";
		}
		if (packageName.replace('\\', '/').indexOf('/', 1) > 0) {
			/*
			 * The Java grammar explicitly enumerates the valid Unicode characters.
			 * We won't repeat that and trust in the compiler 
			 */
			return packageName+" is not a valid Java package name";
		}
		if (selectedRuntime == null) {
			return "A target runtime must be selected";
		}
		for (String name : Arrays.asList(
				earProjectName, 
				ejbProjectName, 
				ejbClientProjectName, 
				servletProjectName)
		) {
			if (isExistingProject(name)) {
				return "Project "+name+" already exists";
			}
		}
		return null;
	}
	
	public List<String> allProjectNames() {
		return Arrays.asList(earProjectName, ejbProjectName, ejbClientProjectName, servletProjectName);
	}
	
	private boolean isExistingProject(String name) {
		return root.getProject(name).exists();
	}
	
	public String getProjectBaseName() {
		return projectBaseName;
	}
	
	public String getEarProjectName() {
		return earProjectName;
	}

	public String getEjbProjectName() {
		return ejbProjectName;
	}

	public String getEjbClientProjectName() {
		return ejbClientProjectName;
	}

	public String getServletProjectName() {
		return servletProjectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Set<IRuntime> getTargetRuntimes() {
		return targetRuntimes;
	}

	public void setTargetRuntimes(Set<IRuntime> targetRuntimes) {
		this.targetRuntimes = targetRuntimes;
	}

	public IRuntime getSelectedRuntime() {
		return selectedRuntime;
	}

	public void setSelectedRuntime(IRuntime selectedRuntime) {
		this.selectedRuntime = selectedRuntime;
	}

	public IWorkspaceRoot getRoot() {
		return root;
	}

	public void setRoot(IWorkspaceRoot root) {
		this.root = root;
	}

	public IWorkspace getWorkspace() {
		return workspace;
	}
}