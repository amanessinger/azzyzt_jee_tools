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

import java.lang.reflect.Field;

import org.eclipse.core.runtime.CoreException;
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
	public IProjectFacetVersion javaFacetVersion = JavaFacet.JAVA_60; // default if no runtime is selected, Galileo has no VERSION_1_6
	
	public IProjectFacet ejbFacet = IJ2EEFacetConstants.EJB_FACET;
	public IProjectFacetVersion ejbFacetVersion = EJB_MINIMUM_FACET_VERSION_WANTED;
	
	public IProjectFacet jpaFacet;
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

		Class<?> jpaPlugin = null;
		try {
			jpaPlugin = Class.forName("org.eclipse.jpt.jpa.core.JpaFacet"); // introduced with Indigo
			Field f = jpaPlugin.getField("FACET");
			jpaFacet = (IProjectFacet) f.get(null);
		} catch (Exception ex) { }
		if (jpaFacet == null) {
			try {
				jpaPlugin = Class.forName("org.eclipse.jpt.core.JptCorePlugin"); // introduced with Indigo
				Field f = jpaPlugin.getField("FACET_ID");
				String facetId = (String) f.get(null);
				jpaFacet = ProjectFacetsManager.getProjectFacet(facetId);
			} catch (Exception ex) { }
		}
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