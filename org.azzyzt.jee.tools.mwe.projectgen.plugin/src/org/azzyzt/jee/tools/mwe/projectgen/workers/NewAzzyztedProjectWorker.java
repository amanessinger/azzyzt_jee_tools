package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;
import org.azzyzt.jee.tools.mwe.projectgen.project.Context;
import org.azzyzt.jee.tools.mwe.projectgen.project.EarProject;
import org.azzyzt.jee.tools.mwe.projectgen.project.Facets;
import org.azzyzt.jee.tools.mwe.projectgen.project.Project;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.core.JpaProject;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.jst.j2ee.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * @author advman
 *
 */
public class NewAzzyztedProjectWorker {
	
	private static final String EJB_SRC_FOLDER_NAME = "ejbModule";
	
	private static final String GENERATED_SRC_FOLDER_NAME = "generated";

	public static final IProjectFacetVersion ejbFacetVersion = IJ2EEFacetConstants.EJB_31;

	private final Context context = new Context();

	public NewAzzyztedProjectWorker() { }

	/**
	 * The main workflow method. It creates all required projects and adds them to an EAR project
	 * 
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public void generate() 
	throws InterruptedException, CoreException 
	{
		context.initializeRuntimeSpecificFacets(this);
		
		if (!context.isValid()) throw new CoreException(context.getFacets().errorStatus);
		
		context.getMonitor().beginTask("Generating EAR project "+context.getEarProjectName(), 100);
		
		try {
			// We crash upon EAR facet creation if the EAR has been created implicitly. Do it now.
			EarProject.create(context.getEarProjectName(), context, (Project[])null);
			
			advanceProgress(10, "Create EJB project");
			
			IProject ejbProject = createEJBProject();
	
			advanceProgress(65, "Fixing EJB client project configuration");
			
			createSourceFolderIfNeededAndAddToProject(context.getEjbClientProjectName(), GENERATED_SRC_FOLDER_NAME);
	
			advanceProgress(70, "Creating servlet project");
			
			createServletProject(ejbProject);
		} finally {
			context.getMonitor().done();
		}
	}

	private IProject createEJBProject()
	throws CoreException, InterruptedException 
	{
		IFacetedProject fprj = createFacetedProject(context.getEjbProjectName());
		
		installJavaFacet(fprj, GENERATED_SRC_FOLDER_NAME);
	
		installEJBFacet(fprj);
		
		IProject prj = fprj.getProject();
		
		// ejbModule has not been added to the class path automatically -> add it now.
		Path ejbSrcPath = createFolderPathIfNeeded(prj, EJB_SRC_FOLDER_NAME);
		addFolderToClassPath(prj, ejbSrcPath);
		
		installJPAFacet(fprj);
		
		installServerSpecificFacets(fprj);
	
		fixFacets(fprj, context.getFacets().javaFacet, context.getFacets().ejbFacet);
		
		createSubpackages(prj, EJB_SRC_FOLDER_NAME, "entity", "service");
		
		buildJavaClass(
				prj, 
				EJB_SRC_FOLDER_NAME, 
				context.getPackageName()+".service", 
				"org.azzyzt.jee.tools.mwe.builder.HelloServiceBeanBuilder", 
				"Creating HelloServiceBean"
		);
		
		buildJavaClass(
				prj, 
				GENERATED_SRC_FOLDER_NAME, 
				context.getPackageName()+".entity", 
				"org.azzyzt.jee.tools.mwe.builder.DefaultStandardEntityListenersBuilder", 
				"Creating initial StandardEntityListeners"
		);
		
		addAzzyztedNature(fprj);
		
		refresh(prj);
		
		return prj;
	}

	private IProject createServletProject(IProject ejbProject)
	throws CoreException, InterruptedException 
	{
		IFacetedProject fprj = createFacetedProject(context.getServletProjectName());
		
		installJavaFacet(fprj, "src", GENERATED_SRC_FOLDER_NAME);
		
		installWebFacet(fprj);
		
		installServerSpecificFacets(fprj);
	
		fixFacets(fprj, context.getFacets().javaFacet, context.getFacets().webFacet);
		
		Util.appendProjectToClassPath(Util.getJavaProject(fprj.getProject()), Util.getJavaProject(ejbProject));
		
		return fprj.getProject();
	}

	private void addAzzyztedNature(IFacetedProject fprj) 
	throws CoreException 
	{
		IProject prj = fprj.getProject();
		
		if (prj.hasNature(Activator.AZZYZTED_NATURE_ID)) return;
		
		IProjectDescription projectDescription = prj.getDescription();
		String[] natureIds = projectDescription.getNatureIds();
		String[] newNatureIds = new String[natureIds.length + 1];
		System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
		newNatureIds[0] = Activator.AZZYZTED_NATURE_ID;
		projectDescription.setNatureIds(newNatureIds);
		prj.setDescription(projectDescription, context.getSubMonitor());
	}

	private void installJavaFacet(IFacetedProject fprj, String...sourceFolderNames)
	throws CoreException 
	{
		JavaFacetInstallConfig config = (JavaFacetInstallConfig) Project.createConfigObject(context.getFacets().javaFacetVersion);
		List<IPath> sourceFolderPaths = new ArrayList<IPath>();
		for (String sourceFolderName : sourceFolderNames) {
			sourceFolderPaths.add((IPath) new Path(sourceFolderName));
		}
		config.setSourceFolders(sourceFolderPaths);
		
		installFacet(fprj, context.getFacets().javaFacetVersion, config);
	}
	

	private void installEJBFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(Facets.ejbFacetVersion);
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				context.getEarProjectName());
		config.setBooleanProperty(
				IEjbFacetInstallDataModelProperties.CREATE_CLIENT,
				Boolean.TRUE);

		installFacet(fprj, Facets.ejbFacetVersion, config);
	}
	

	private void installUtilityFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(context.getFacets().utilityFacetVersion);
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				context.getEarProjectName());
		
		installFacet(fprj, context.getFacets().utilityFacetVersion, config);
	}
	

	private void installWebFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(context.getFacets().webFacetVersion);
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				context.getEarProjectName());
		
		installFacet(fprj, context.getFacets().webFacetVersion, config);
	}
	

	private void installServerSpecificFacets(IFacetedProject fprj)
	throws CoreException 
	{
		if (context.getSelectedRuntime().supports(context.getFacets().sunFacet)) {
			installGlassFishFacet(fprj);
		}
	}
	
	
	private void installGlassFishFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		installFacet(fprj, context.getFacets().sunFacetVersion, null);
	}
	

	private void installJPAFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(context.getFacets().jpaFacetVersion);
		
		installFacet(fprj, context.getFacets().jpaFacetVersion, config);

		/*
		 *  TODO make sure we get the highest EclipseLink 2.1 ???
		 *  Sometimes we get it, sometimes we just get Generic 2.0. Find out why.
		 */
		IProject prj = fprj.getProject();
		JpaProject jpaProject = JptCorePlugin.getJpaProject(prj);
		//JptCorePlugin.setJpaPlatformId(prj, "eclipselink2_1");
		//jpaProject.update();
	}
	

	private IFacetedProject createFacetedProject(String prjName)
	throws CoreException 
	{
		IProject prj = context.getRoot().getProject(prjName);
		IProjectDescription projectDescription = context.getWorkspace()
				.newProjectDescription(prj.getName());

		if (!prj.exists()) {
			// Contained projects will create the EAR project due to a forward reference
			prj.create(projectDescription, context.getSubMonitor());
		}
		prj.open(context.getSubMonitor());

		IFacetedProject factetedPrj = ProjectFacetsManager.create(prj, true,
				context.getSubMonitor());

		// associate runtime, we need it for all project types
		factetedPrj.setTargetedRuntimes(context.getTargetRuntimes(), null);
		factetedPrj.setPrimaryRuntime(context.getSelectedRuntime(), null);

		return factetedPrj;
	}
	

	private void installFacet(
			IFacetedProject fprj,
			IProjectFacetVersion facetVersion, 
			Object config)
	throws CoreException 
	{
		fprj.installProjectFacet(
				facetVersion, 
				config,
				context.getSubMonitor()
		);
	}
	
	
	private void fixFacets(IFacetedProject fprj, IProjectFacet...facets) 
	throws CoreException 
	{
		fprj.setFixedProjectFacets(new HashSet<IProjectFacet>(Arrays.asList(facets)));
	}
	

	private void refresh(IProject prj) throws CoreException {
		prj.refreshLocal(IResource.DEPTH_INFINITE, context.getSubMonitor());
	}
	

	private void createSourceFolderIfNeededAndAddToProject(String prjName, String folderName)
	throws CoreException, InterruptedException 
	{
		IProject prj = openExistingProject(prjName);
		
		createSourceFolderIfNeededAndAddToProject(prj, folderName);
	}
	

	private void createSourceFolderIfNeededAndAddToProject(IProject prj,
			String folderName) throws CoreException, JavaModelException {
		Path path = createFolderPathIfNeeded(prj, folderName);
		
		addFolderToClassPath(prj, path);
	}
	

	private void addFolderToClassPath(IProject prj, Path path)
			throws CoreException, JavaModelException {
		IClasspathEntry classpathEntry = JavaCore.newSourceEntry(prj.getFullPath().append(path));
		IJavaProject jprj = Util.getJavaProject(prj);
		IClasspathEntry[] rawClasspath = jprj.getRawClasspath();
		IClasspathEntry[] newClasspath = new IClasspathEntry[rawClasspath.length + 1];
		newClasspath[0] = classpathEntry;
		System.arraycopy(rawClasspath, 0, newClasspath, 1, rawClasspath.length);
		jprj.setRawClasspath(newClasspath, context.getSubMonitor());
	}
	

	private Path createFolderPathIfNeeded(IProject prj, String folderName)
			throws CoreException {
		Path path = new Path(folderName);
		createFolderForPathIfNeeded(prj, path);
		return path;
	}
	

	private IFolder createFolderForPathIfNeeded(IProject prj, Path path)
			throws CoreException {
		IFolder f = prj.getFolder(path);
		if (!f.exists()) {
			f.create(true, true, context.getSubMonitor());
		}
		return f;
	}
	

	private void createSubpackages(IProject prj, String srcFolderName, String...pkgNames) 
	throws CoreException, JavaModelException 
	{
		IJavaProject jprj = Util.getJavaProject(prj);
		IPath absEjbSrcPath = prj.getFolder(srcFolderName).getFullPath();
		IPackageFragmentRoot ejbPackageFragmentRoot = jprj.findPackageFragmentRoot(absEjbSrcPath);
		for (String subPackage : Arrays.asList(pkgNames)) {
			String pkgName = String.format("%s.%s", context.getPackageName(), subPackage);
			ejbPackageFragmentRoot.createPackageFragment(pkgName, true, context.getSubMonitor());
		}
	}
	

	private void copyFromUrlToFolder(IContainer iContainer, URL content, String fileName) 
	throws IOException, CoreException 
	{
		InputStream in = content.openConnection().getInputStream();
		iContainer.getFile(new Path(fileName)).create(in, true, context.getSubMonitor());
	}
	

	private IProject openExistingProject(String prjName) 
	throws CoreException 
	{
		IProject prj = context.getRoot().getProject(prjName);
		if (!prj.exists()) {
			IStatus status = new Status(
					IStatus.ERROR, 
					Activator.PLUGIN_ID, 
					"Project "+prjName+" should exist but doesn't"
					);
			throw new CoreException(status);
		}
		prj.open(context.getSubMonitor());
		return prj;
	}
	

	private void advanceProgress(int amountFinished, String nowBeginning) 
	throws InterruptedException 
	{
		IProgressMonitor monitor = context.getMonitor();
		if (monitor.isCanceled()) {
			monitor.done();
			throw new InterruptedException();
		}
		monitor.worked(amountFinished);
		monitor.subTask(nowBeginning);
	}
	

	private void buildJavaClass(
			IProject prj, 
			String srcFolderName, 
			String pkgName, 
			String fqBuilder, 
			String jobTitle) 
	throws InterruptedException, CoreException 
	{
		IFolder srcFolder = prj.getFolder(srcFolderName);
		IPath srcFolderPath = srcFolder.getLocation();
		
		URL[] classPathEntries = Util.classPathURLsForToolMainClass(prj);
		String fqMainClassName = "org.azzyzt.jee.tools.mwe.GenericGenerator";
		String[] args = {
				srcFolderPath.toString(), 
				pkgName, 
				fqBuilder
		};

		Util.callExternalMainClass(jobTitle, classPathEntries, fqMainClassName, args);		
	}

	public Context getContext() {
		return context;
	}

}
