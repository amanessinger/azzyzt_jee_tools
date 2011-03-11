package org.azzyzt.jee.tools.mwe.projectgen.project;

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
