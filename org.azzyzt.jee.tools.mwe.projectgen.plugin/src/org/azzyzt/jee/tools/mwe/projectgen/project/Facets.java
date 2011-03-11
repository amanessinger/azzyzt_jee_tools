package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class Facets {
	public static final IProjectFacetVersion EJB_FACET_VERSION_WANTED = IJ2EEFacetConstants.EJB_31;
	
	public Set<IProjectFacetVersion> facetVersionsNeeded;
	public IProjectFacet javaFacet;
	public IProjectFacetVersion javaFacetVersion;
	public IProjectFacet ejbFacet;
	public IProjectFacetVersion ejbFacetVersion = EJB_FACET_VERSION_WANTED;
	public IProjectFacet jpaFacet;
	public IProjectFacetVersion jpaFacetVersion;
	public IProjectFacet earFacet;
	public IProjectFacetVersion earFacetVersion;
	public IProjectFacet utilityFacet;
	public IProjectFacetVersion utilityFacetVersion;
	public IProjectFacet webFacet;
	public IProjectFacetVersion webFacetVersion;
	public IProjectFacet sunFacet;
	public IProjectFacetVersion sunFacetVersion;
	public IStatus errorStatus;

	public Facets(Set<IProjectFacetVersion> facetVersionsNeeded) {
		this.facetVersionsNeeded = facetVersionsNeeded;
	}

	public boolean successfullyInitializedFacets() {
		javaFacetVersion = JavaFacet.VERSION_1_6;
		javaFacet = javaFacetVersion.getProjectFacet();
		
		ejbFacet = ejbFacetVersion.getProjectFacet();
		
		jpaFacet = ProjectFacetsManager.getProjectFacet(JptCorePlugin.FACET_ID);
		Set<IProjectFacetVersion> jpaFacetVersions;
		try {
			jpaFacetVersions = jpaFacet.getVersions(JptCorePlugin.JPA_FACET_VERSION_2_0);
			Iterator<IProjectFacetVersion> fvIterator = jpaFacetVersions.iterator();
			 // we take the first we get, users can always change facet settings later
			jpaFacetVersion = fvIterator.next();
		} catch (CoreException e) {
			errorStatus = e.getStatus();
			return false;
		}
		
		earFacetVersion = IJ2EEFacetConstants.ENTERPRISE_APPLICATION_60;
		earFacet = earFacetVersion.getProjectFacet();
		
		utilityFacetVersion = IJ2EEFacetConstants.UTILITY_FACET_10;
		utilityFacet = utilityFacetVersion.getProjectFacet();
		
		webFacetVersion = IJ2EEFacetConstants.DYNAMIC_WEB_30;
		webFacet = webFacetVersion.getProjectFacet();
		
		return true;
	}
}