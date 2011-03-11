package org.azzyzt.jee.tools.mwe.projectgen.workers;

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
	public Set<IProjectFacetVersion> facetVersionsNeeded;
	public boolean isValid;
	public IProjectFacet javaFacet;
	public IProjectFacetVersion javaFacetVersion;
	public IProjectFacet ejbFacet;
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

	public Facets(Set<IProjectFacetVersion> facetVersionsNeeded, boolean isValid) {
		this.facetVersionsNeeded = facetVersionsNeeded;
		this.isValid = isValid;
	}

	public boolean successfullyInitializedFacets(NewAzzyztedProjectWorker newAzzyztedProjectWorker) {
		javaFacetVersion = JavaFacet.VERSION_1_6;
		javaFacet = javaFacetVersion.getProjectFacet();
		
		ejbFacet = NewAzzyztedProjectWorker.ejbFacetVersion.getProjectFacet();
		
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