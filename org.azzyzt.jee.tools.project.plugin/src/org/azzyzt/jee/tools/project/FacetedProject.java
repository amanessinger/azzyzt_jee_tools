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
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class FacetedProject extends Project {
	
	private IFacetedProject fp;
	
	protected FacetedProject() { }
	
	public FacetedProject(String name, Context context)
	throws CoreException 
	{
		super(name, context);

		fp = ProjectFacetsManager.create(p, true, context.getSubMonitor());
		
		if (context.getSelectedRuntime() != null) {
			/*
			 *  associate runtime, we need it for all project types. Upon project creation, 
			 *  we get a selected runtime, if we merely open an existing project, runtimes
			 *  are already set
			 */
			fp.setTargetedRuntimes(context.getTargetRuntimes(), null);
			fp.setPrimaryRuntime(context.getSelectedRuntime(), null);
		}
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
	
	public static Object createConfigObject(IProjectFacetVersion fv) 
	throws CoreException 
	{
		IActionDefinition installAction = fv.getActionDefinition(
				Collections.singleton(fv),
				IFacetedProject.Action.Type.INSTALL
		);
		return installAction.createConfigObject();
	}

	protected void installServerSpecificFacets() throws CoreException {
		if (getContext().getFacets().sunFacetVersion != null) {
			installGlassFishFacet();
		}
	}

	public IFacetedProject getFp() {
		return fp;
	}
	
	public void setFp(IFacetedProject fp) {
		this.fp = fp;
	}
}

