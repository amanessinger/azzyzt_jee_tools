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

package org.azzyzt.jee.tools.project;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class Project {
	
	private String name;
	private Context context;
	private IProject p;
	private IFacetedProject fp;
	
	protected Project() { }
	
	public Project(String name, Context context)
	throws CoreException 
	{
		this.context = context;
		
		p = context.getRoot().getProject(name);

		if (!p.exists()) {
			IProjectDescription dsc = context.getWorkspace().newProjectDescription(name);
			p.create(dsc, context.getSubMonitor());
		}
		p.open(context.getSubMonitor());

		fp = ProjectFacetsManager.create(p, true, context.getSubMonitor());

		// associate runtime, we need it for all project types
		fp.setTargetedRuntimes(context.getTargetRuntimes(), null);
		fp.setPrimaryRuntime(context.getSelectedRuntime(), null);
	}

	protected void installFacet(
			IProjectFacetVersion facetVersion, 
			Object config)
	throws CoreException 
	{
		fp.installProjectFacet(
				facetVersion, 
				config,
				context.getSubMonitor()
		);
	}
	
	protected void installGlassFishFacet() 
	throws CoreException 
	{
		installFacet(getContext().getFacets().sunFacetVersion, null);
	}
	
	protected void fixFacets(IProjectFacet...facets) 
	throws CoreException 
	{
		fp.setFixedProjectFacets(new HashSet<IProjectFacet>(Arrays.asList(facets)));
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

	public static Object createConfigObject(IProjectFacetVersion fv) 
	throws CoreException 
	{
		IActionDefinition installAction = fv.getActionDefinition(
				Collections.singleton(fv),
				IFacetedProject.Action.Type.INSTALL
		);
		return installAction.createConfigObject();
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

	protected void installServerSpecificFacets() throws CoreException {
		if (getContext().getFacets().sunFacetVersion != null) {
			installGlassFishFacet();
		}
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
	
	public IFacetedProject getFp() {
		return fp;
	}
	
	public void setFp(IFacetedProject fp) {
		this.fp = fp;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
}

