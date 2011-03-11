package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.projectgen.workers.Util;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;

public class Context {
	
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
	private IProgressMonitor monitor = null;
	private Facets facets = new Facets(new HashSet<IProjectFacetVersion>());
	private Boolean createEjbClient = true;
	private boolean isValid = false;
	
	public Context() {
		workspace = ResourcesPlugin.getWorkspace();
		root = workspace.getRoot();
		setValid(successfullyInitialized());
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

	public void setFacets(Facets facets) {
		this.facets = facets;
	}

	public Facets getFacets() {
		return facets;
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

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public IProgressMonitor getSubMonitor() {
		/*
		 * TODO this constant actually does not make sense. Read it as "some".
		 * Individually adjust it, depending upon the ticks of the main sub-tasks
		 * and the number of invocations. Or live with the mess. Progress reporting
		 * is a mess anyway. 
		 */
		return new SubProgressMonitor(monitor, 2);
		//return null; // TODO read http://www.eclipse.org/resources/resource.php?id=139 and do the right thing :) 
	}

	public boolean successfullyInitializedRuntimes() {
		// We could be more specific, but the rest more or less follows automatically
		getFacets().facetVersionsNeeded.add(getFacets().ejbFacetVersion);
		getFacets().facetVersionsNeeded.add(getFacets().jpaFacetVersion);
	
		setTargetRuntimes(RuntimeManager.getRuntimes(getFacets().facetVersionsNeeded));
		if (getTargetRuntimes().isEmpty()) {
			getFacets().errorStatus = Util.createErrorStatus("No runtime supporting the needed facets available");
			return false;
		}
		
		return true;
	}

	public void initializeRuntimeSpecificFacets() 
	throws CoreException 
	{
		// TODO add support for other JEE 6+ servers
		getFacets().sunFacet = ProjectFacetsManager.getProjectFacet("sun.facet");
		getFacets().sunFacetVersion = getFacets().sunFacet.getLatestSupportedVersion(getSelectedRuntime());
	}

	public boolean successfullyInitialized() {
		
		if (!getFacets().successfullyInitializedFacets()) return false;
	
		if (!successfullyInitializedRuntimes()) return false;
	
		return true;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setCreateEjbClient(Boolean createEjbClient) {
		this.createEjbClient = createEjbClient;
	}

	public Boolean getCreateEjbClient() {
		return createEjbClient;
	}
}