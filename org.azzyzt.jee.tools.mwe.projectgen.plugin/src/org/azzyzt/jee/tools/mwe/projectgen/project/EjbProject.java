package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.util.Arrays;
import java.util.List;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.j2ee.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class EjbProject extends JavaProject {

	public static final String EJB_SRC_FOLDER_NAME = "ejbModule";
	
	private JavaProject client;

	public EjbProject(
			String name, 
			Context context, 
			EarProject ear,
			List<JavaProject> projectsOnBuildPath) 
	throws CoreException, InterruptedException 
	{
		super(name, context, Arrays.asList(EJB_SRC_FOLDER_NAME, GENERATED_SRC_FOLDER_NAME));
		
		addProjectsToBuildPath(projectsOnBuildPath);
		installEJBFacet(ear);
		installJpaFacet();
		installServerSpecificFacets();
		moveJreToEndOfClassPath();
		fixFacets(context.getFacets().javaFacet, context.getFacets().ejbFacet);
		createSubpackages(EJB_SRC_FOLDER_NAME, "entity", "service");
		
		buildJavaClass(
				EJB_SRC_FOLDER_NAME, 
				context.getPackageName()+".service", 
				"org.azzyzt.jee.tools.mwe.builder.HelloServiceBeanBuilder", 
				"Creating HelloServiceBean"
		);
		
		buildJavaClass(
				GENERATED_SRC_FOLDER_NAME, 
				context.getPackageName()+".entity", 
				"org.azzyzt.jee.tools.mwe.builder.DefaultStandardEntityListenersBuilder", 
				"Creating initial StandardEntityListeners"
		);
		
		addMarkerNature(Activator.AZZYZTED_NATURE_ID);
		
		refresh();
	}

	private void installEJBFacet(EarProject ear) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(getContext().getFacets().ejbFacetVersion);

		Boolean addToEar = (ear != null);
		boolean createClientProject = getContext().getCreateEjbClient() && addToEar;
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				addToEar);
		if (addToEar) {
			config.setStringProperty(
					IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
					getContext().getEarProjectName());
		}
		config.setBooleanProperty(
				IEjbFacetInstallDataModelProperties.CREATE_CLIENT,
				createClientProject
		);

		installFacet(getContext().getFacets().ejbFacetVersion, config);
		
		if (createClientProject) {
			Project cp = new Project(getContext().getEjbClientProjectName(), getContext());
			client = JavaProject.asJavaProject(cp);
			client.createFolderPathIfNeeded(GENERATED_SRC_FOLDER_NAME);
			client.addFolderToClassPath(createFolderPathIfNeeded(GENERATED_SRC_FOLDER_NAME));
			client.moveJreToEndOfClassPath();
		}
	}
	
}
