package org.azzyzt.jee.tools.mwe.projectgen.project;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

/**
 * Requesting the facets available with Eclipse Helios
 */
public class Facets {
	/**
	 * This is the baseline. We won't generate code that goes below that
	 */
	public static final IProjectFacetVersion EJB_MINIMUM_FACET_VERSION_WANTED = IJ2EEFacetConstants.EJB_30;
	
	public IProjectFacet javaFacet = JavaFacet.FACET;
	public IProjectFacetVersion javaFacetVersion;
	
	public IProjectFacet ejbFacet = IJ2EEFacetConstants.EJB_FACET;
	public IProjectFacetVersion ejbFacetVersion = EJB_MINIMUM_FACET_VERSION_WANTED;
	
	public IProjectFacet jpaFacet = ProjectFacetsManager.getProjectFacet(JptCorePlugin.FACET_ID);
	public IProjectFacetVersion jpaFacetVersion;
	
	public IProjectFacet earFacet = IJ2EEFacetConstants.ENTERPRISE_APPLICATION_FACET;
	public IProjectFacetVersion earFacetVersion;
	
	public IProjectFacet utilityFacet = IJ2EEFacetConstants.UTILITY_FACET;
	public IProjectFacetVersion utilityFacetVersion;
	
	public IProjectFacet webFacet = IJ2EEFacetConstants.DYNAMIC_WEB_FACET;
	public IProjectFacetVersion webFacetVersion;
	
	public IProjectFacet sunFacet = null;
	public IProjectFacetVersion sunFacetVersion = null;
	
	public Facets() { }

	public boolean initializeFacets(IRuntime selectedRuntime) 
	throws CoreException 
	{
		javaFacetVersion = javaFacet.getLatestSupportedVersion(selectedRuntime);
		ejbFacetVersion = ejbFacet.getLatestSupportedVersion(selectedRuntime);
		jpaFacetVersion = jpaFacet.getLatestSupportedVersion(selectedRuntime);
		earFacetVersion = earFacet.getLatestSupportedVersion(selectedRuntime);		
		utilityFacetVersion = utilityFacet.getLatestSupportedVersion(selectedRuntime);
		webFacetVersion = webFacet.getLatestSupportedVersion(selectedRuntime);
		
		try {
			// TODO add support for other JEE 6+ servers
			sunFacet = ProjectFacetsManager.getProjectFacet("sun.facet");
			if (selectedRuntime.supports(sunFacet)) {
				sunFacetVersion = sunFacet.getLatestSupportedVersion(selectedRuntime);
			}
		} catch (IllegalArgumentException e) {
			// OK. Users will have to check sever-specific facet versions for null
		}
		
		return true;
	}
}