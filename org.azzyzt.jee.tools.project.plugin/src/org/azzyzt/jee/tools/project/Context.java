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

package org.azzyzt.jee.tools.project;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.tools.common.Util;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
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
	private Facets facets = new Facets();
	private Boolean createEjbClient = true;
	private boolean isValid = false;
	private IStatus errorStatus;
	
	public Context() {
		workspace = ResourcesPlugin.getWorkspace();
		root = workspace.getRoot();
		setValid(successfullyInitializedRuntimes());
	}

	public IStatus getErrorStatus() {
		return errorStatus;
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
			return "FacetedProject base name must not be empty";
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
				return "FacetedProject "+name+" already exists";
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
		setTargetRuntimes(RuntimeManager.getRuntimes(
				Collections.singleton(Facets.EJB_MINIMUM_FACET_VERSION_WANTED)
				)
		);
		if (getTargetRuntimes().isEmpty()) {
			errorStatus = Util.createErrorStatus("No runtime supporting the needed facets available");
			return false;
		}
		
		return true;
	}

	public void initializeRuntimeSpecificFacets() 
	throws CoreException 
	{
		facets.initializeFacets(selectedRuntime);
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