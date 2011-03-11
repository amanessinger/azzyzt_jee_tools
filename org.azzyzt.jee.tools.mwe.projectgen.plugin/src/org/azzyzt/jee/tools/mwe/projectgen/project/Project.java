package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action.Type;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class Project {
	
	private String name;
	private Context context;
	private IProject p;
	private IFacetedProject fp;

	public Project(String name, Context context)
	throws CoreException 
	{
		p = context.getRoot().getProject(name);
		IProjectDescription dsc = context.getWorkspace().newProjectDescription(name);

		if (!p.exists()) {
			p.create(dsc, context.getSubMonitor());
		}
		p.open(context.getSubMonitor());

		fp = ProjectFacetsManager.create(p, true, context.getSubMonitor());

		// associate runtime, we need it for all project types
		fp.setTargetedRuntimes(context.getTargetRuntimes(), null);
		fp.setPrimaryRuntime(context.getSelectedRuntime(), null);
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
		installFacet(getContext().facets.sunFacetVersion, null);
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
}
