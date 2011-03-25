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
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class DynamicWebProject extends JavaProject {

	public DynamicWebProject(
			String name, 
			Context context, 
			EarProject ear,
			List<JavaProject> projectsOnBuildPath) 
	throws CoreException 
	{
		super(name, context, Arrays.asList(DWP_SRC_FOLDER_NAME, GENERATED_SRC_FOLDER_NAME));

		addProjectsToBuildPath(projectsOnBuildPath);
		installWebFacet(ear);
		installServerSpecificFacets();
		fixFacets(context.getFacets().javaFacet, context.getFacets().webFacet);
		moveJreToEndOfClassPath();
	}
	
	private void installWebFacet(EarProject ear) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(
				getContext().getFacets().webFacetVersion
		);
		
		Boolean addToEar = (ear != null);
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				addToEar);
		if (addToEar) {
			config.setStringProperty(
					IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
					getContext().getEarProjectName());
		}
		
		installFacet(getContext().getFacets().webFacetVersion, config);
	}
	
}
