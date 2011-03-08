package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;
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
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.jst.j2ee.earcreation.IEarFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.internal.project.facet.EARFacetProjectCreationDataModelProvider;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.FacetProjectCreationDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;

/**
 * @author advman
 *
 */
public class NewAzzyztedProjectWorker {
	
	private static final String EJB_SRC_FOLDER_NAME = "ejbModule";
	
	private static final String GENERATED_SRC_FOLDER_NAME = "generated";

	public Set<IProjectFacetVersion> facetVersionsNeeded = new HashSet<IProjectFacetVersion>();
	
	private static final IProjectFacetVersion ejbFacetVersion = IJ2EEFacetConstants.EJB_31;

	private boolean isValid = false;
	
	private IProgressMonitor monitor;
		
	private final AzzyztedProjectParameters parameters = new AzzyztedProjectParameters();

	private IProjectFacet javaFacet;
	private IProjectFacetVersion javaFacetVersion;

	private IProjectFacet ejbFacet;
	
	private IProjectFacet jpaFacet;
	private IProjectFacetVersion jpaFacetVersion;

	private IProjectFacet earFacet;
	private IProjectFacetVersion earFacetVersion;

	private IProjectFacet utilityFacet;
	private IProjectFacetVersion utilityFacetVersion;

	private IProjectFacet webFacet;
	private IProjectFacetVersion webFacetVersion;

	private IProjectFacet sunFacet;
	private IProjectFacetVersion sunFacetVersion;

	private IStatus errorStatus;

	public NewAzzyztedProjectWorker() {
		isValid = successfullyInitialized();
	}

	private boolean successfullyInitialized() {
		
		if (!successfullyInitializedFacets()) return false;

		if (!successfullyInitializedRuntimes()) return false;

		return true;
	}

	private boolean successfullyInitializedFacets() {
		javaFacetVersion = JavaFacet.VERSION_1_6;
		javaFacet = javaFacetVersion.getProjectFacet();
		
		ejbFacet = ejbFacetVersion.getProjectFacet();
		
		jpaFacet = ProjectFacetsManager.getProjectFacet(JptCorePlugin.FACET_ID);
		Set<IProjectFacetVersion> jpaFacetVersions;
		try {
			jpaFacetVersions = jpaFacet.getVersions(JptCorePlugin.JPA_FACET_VERSION_2_0);
			jpaFacetVersion = jpaFacetVersions.iterator().next(); // TODO make this configurable?
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

	private boolean successfullyInitializedRuntimes() {
		// We could be more specific, but the rest more or less follows automatically
		facetVersionsNeeded.add(ejbFacetVersion);
		facetVersionsNeeded.add(jpaFacetVersion);

		parameters.setTargetRuntimes(RuntimeManager.getRuntimes(facetVersionsNeeded));
		if (parameters.getTargetRuntimes().isEmpty()) {
			errorStatus = Util.createErrorStatus("No runtime supporting the needed facets available");
			return false;
		}
		
		return true;
	}

	private void initializeRuntimeSpecificFacets() 
	throws CoreException 
	{
		// TODO add support for other JEE 6+ servers
		sunFacet = ProjectFacetsManager.getProjectFacet("sun.facet");
		sunFacetVersion = sunFacet.getLatestSupportedVersion(parameters.getSelectedRuntime());
	}
	
	/**
	 * The main workflow method. It creates all required projects and adds them to an EAR project
	 * 
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public void generate() 
	throws InterruptedException, CoreException 
	{
		initializeRuntimeSpecificFacets();
		
		if (!isValid) throw new CoreException(errorStatus);
		
		monitor.beginTask("Generating EAR project "+parameters.getEarProjectName(), 100);
		
		try {
			// We crash upon EAR facet creation if the EAR has been created implicitly. Do it now.
			createEARProject();
			
			advanceProgress(10, "Create EJB project");
			
			IProject ejbProject = createEJBProject();
	
			advanceProgress(65, "Fixing EJB client project configuration");
			
			createSourceFolderIfNeededAndAddToProject(parameters.getEjbClientProjectName(), GENERATED_SRC_FOLDER_NAME);
	
			advanceProgress(70, "Creating servlet project");
			
			createServletProject(ejbProject);
		} finally {
			monitor.done();
		}
	}

	private IProject createEJBProject()
	throws CoreException, InterruptedException 
	{
		IFacetedProject fprj = createFacetedProject(parameters.getEjbProjectName());
		
		installJavaFacet(fprj, GENERATED_SRC_FOLDER_NAME);
	
		installEJBFacet(fprj);
		
		IProject prj = fprj.getProject();
		
		// ejbModule has not been added to the class path automatically -> add it now.
		Path ejbSrcPath = createFolderPathIfNeeded(prj, EJB_SRC_FOLDER_NAME);
		addFolderToClassPath(prj, ejbSrcPath);
		
		installJPAFacet(fprj);
		
		installServerSpecificFacets(fprj);
	
		fixFacets(fprj, javaFacet, ejbFacet);
		
		createSubpackages(prj, EJB_SRC_FOLDER_NAME, "entity", "service");
		
		buildJavaClass(
				prj, 
				EJB_SRC_FOLDER_NAME, 
				parameters.getPackageName()+".service", 
				"org.azzyzt.jee.tools.mwe.builder.HelloServiceBeanBuilder", 
				"Creating HelloServiceBean"
		);
		
		buildJavaClass(
				prj, 
				GENERATED_SRC_FOLDER_NAME, 
				parameters.getPackageName()+".entity", 
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
		IFacetedProject fprj = createFacetedProject(parameters.getServletProjectName());
		
		installJavaFacet(fprj, "src", GENERATED_SRC_FOLDER_NAME);
		
		installWebFacet(fprj);
		
		installServerSpecificFacets(fprj);
	
		fixFacets(fprj, javaFacet, webFacet);
		
		Util.appendProjectToClassPath(Util.getJavaProject(fprj.getProject()), Util.getJavaProject(ejbProject));
		
		return fprj.getProject();
	}

	private void createEARProject(IProject...projects) 
	throws CoreException, InterruptedException 
	{
		IFacetedProject fprj = createFacetedProject(parameters.getEarProjectName());
		
		installEARFacet(fprj, projects);
	
		installServerSpecificFacets(fprj);
		
		fixFacets(fprj, IJ2EEFacetConstants.ENTERPRISE_APPLICATION_FACET);

		installRuntimeLibsIntoEar(fprj);
	}

	private void installRuntimeLibsIntoEar(IFacetedProject fprj) 
	throws CoreException 
	{
		IProject prj = fprj.getProject();
		IFolder lib = createFolderForPathIfNeeded(prj, new Path("lib"));
		
		try {
			copyFromUrlToFolder(lib, Activator.getJeeRuntimeJarUrl(), Activator.JEE_RUNTIME_JAR);
			copyFromUrlToFolder(lib, Activator.getJeeRuntimeSiteJarUrl(), Activator.JEE_RUNTIME_SITE_JAR);
		} catch (IOException e) {
			throw Util.createCoreException("Can't install runtime libraries into EAR project", e);
		}
		IVirtualComponent earCmp = ComponentCore.createComponent(prj);
		
		// get current references
		List<IVirtualReference> references = new ArrayList<IVirtualReference>();
		IVirtualReference[] currentReferences = earCmp.getReferences();
		for (IVirtualReference reference : currentReferences) {
			references.add(reference);
		}
		
		String handlePrfx;
		IVirtualComponent jarCmp;
		IVirtualReference jarRef;

		handlePrfx = VirtualArchiveComponent.LIBARCHIVETYPE + IPath.SEPARATOR
				+ prj.getName() + IPath.SEPARATOR + "lib" + IPath.SEPARATOR;
		jarCmp = ComponentCore.createArchiveComponent(prj, handlePrfx + Activator.JEE_RUNTIME_JAR);
		jarRef = ComponentCore.createReference(earCmp, jarCmp, new Path("/lib"));
		if (!references.contains(jarRef)) {
			references.add(jarRef);
		}
		
		handlePrfx = VirtualArchiveComponent.LIBARCHIVETYPE + IPath.SEPARATOR
				+ prj.getName() + IPath.SEPARATOR + "lib" + IPath.SEPARATOR;
		jarCmp = ComponentCore.createArchiveComponent(prj, handlePrfx + Activator.JEE_RUNTIME_SITE_JAR);
		jarRef = ComponentCore.createReference(earCmp, jarCmp, new Path("/lib"));
		if (!references.contains(jarRef)) {
			references.add(jarRef);
		}

		earCmp.setReferences(references.toArray(new IVirtualReference[references.size()]));
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
		prj.setDescription(projectDescription, getSubMonitor());
	}

	private void installJavaFacet(IFacetedProject fprj, String...sourceFolderNames)
	throws CoreException 
	{
		JavaFacetInstallConfig config = (JavaFacetInstallConfig) createConfigObject(javaFacetVersion);
		List<IPath> sourceFolderPaths = new ArrayList<IPath>();
		for (String sourceFolderName : sourceFolderNames) {
			sourceFolderPaths.add((IPath) new Path(sourceFolderName));
		}
		config.setSourceFolders(sourceFolderPaths);
		
		installFacet(fprj, javaFacetVersion, config);
	}
	

	private void installEJBFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(ejbFacetVersion);
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				parameters.getEarProjectName());
		config.setBooleanProperty(
				IEjbFacetInstallDataModelProperties.CREATE_CLIENT,
				Boolean.TRUE);

		installFacet(fprj, ejbFacetVersion, config);
	}
	

	private void installUtilityFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(utilityFacetVersion);
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				parameters.getEarProjectName());
		
		installFacet(fprj, utilityFacetVersion, config);
	}
	

	private void installWebFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(webFacetVersion);
		
		config.setBooleanProperty(
				IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR,
				Boolean.TRUE);
		config.setStringProperty(
				IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME,
				parameters.getEarProjectName());
		
		installFacet(fprj, webFacetVersion, config);
	}
	

	private void installServerSpecificFacets(IFacetedProject fprj)
	throws CoreException 
	{
		if (parameters.getSelectedRuntime().supports(sunFacet)) {
			installGlassFishFacet(fprj);
		}
	}
	
	
	private void installGlassFishFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		installFacet(fprj, sunFacetVersion, null);
	}
	

	private void installJPAFacet(IFacetedProject fprj) 
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(jpaFacetVersion);
		
		installFacet(fprj, jpaFacetVersion, config);
	}
	

	private void installEARFacet(IFacetedProject fprj, IProject...projects)
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_60);
	
		config.setProperty(
				IEarFacetInstallDataModelProperties.J2EE_PROJECTS_LIST, 
				Arrays.asList(projects)
		);
		config.setProperty(
				IEarFacetInstallDataModelProperties.JAVA_PROJECT_LIST,
				Collections.EMPTY_LIST
		);
		config.setBooleanProperty(
				IFacetDataModelProperties.SHOULD_EXECUTE, Boolean.TRUE);
	
		IDataModel master = DataModelFactory.createDataModel(
				new EARFacetProjectCreationDataModelProvider()
		);
		master.setStringProperty(
				IFacetDataModelProperties.FACET_PROJECT_NAME,
				config.getStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME)
		);
	
		master.setProperty(
				IFacetProjectCreationDataModelProperties.FACET_DM_MAP,
				Collections.singletonMap(
						IJ2EEFacetConstants.ENTERPRISE_APPLICATION,
						config
				)
		);
		master.setProperty(
				IFacetProjectCreationDataModelProperties.FACET_ACTION_MAP,
				Collections.EMPTY_MAP
		);
		master.setProperty(
				FacetProjectCreationDataModelProvider.REQUIRED_FACETS_COLLECTION,
				Collections.singletonList(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_FACET)
		);
	
		config.setProperty(
				FacetInstallDataModelProvider.MASTER_PROJECT_DM, 
				master
		);
	
		installFacet(fprj, IJ2EEFacetConstants.ENTERPRISE_APPLICATION_60, config);
	}
	

	private Object createConfigObject(IProjectFacetVersion fv) 
	throws CoreException 
	{
		IActionDefinition installAction = fv.getActionDefinition(
				Collections.singleton(fv),
				IFacetedProject.Action.Type.INSTALL
		);
		return installAction.createConfigObject();
	}
	

	private IFacetedProject createFacetedProject(String prjName)
	throws CoreException 
	{
		IProject prj = parameters.getRoot().getProject(prjName);
		IProjectDescription ejbProjectDescription = parameters.getWorkspace()
				.newProjectDescription(prj.getName());

		if (!prj.exists()) {
			// Contained projects will create the EAR project due to a forward reference
			prj.create(ejbProjectDescription, getSubMonitor());
		}
		prj.open(getSubMonitor());

		IFacetedProject factetedPrj = ProjectFacetsManager.create(prj, true,
				getSubMonitor());

		// associate runtime, we need it for all project types
		factetedPrj.setTargetedRuntimes(parameters.getTargetRuntimes(), null);
		factetedPrj.setPrimaryRuntime(parameters.getSelectedRuntime(), null);

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
				getSubMonitor()
		);
	}
	
	
	private IProgressMonitor getSubMonitor() {
		/*
		 * TODO this constant actually does not make sense. Read it as "some".
		 * Individually adjust it, depending upon the ticks of the main sub-tasks
		 * and the number of invocations. Or live with the mess. Progress reporting
		 * is a mess anyway. 
		 */
		return new SubProgressMonitor(monitor, 2);
		//return null; // TODO read http://www.eclipse.org/resources/resource.php?id=139 and do the right thing :) 
	}
	

	private void fixFacets(IFacetedProject fprj, IProjectFacet...facets) 
	throws CoreException 
	{
		fprj.setFixedProjectFacets(new HashSet<IProjectFacet>(Arrays.asList(facets)));
	}
	

	private void refresh(IProject prj) throws CoreException {
		prj.refreshLocal(IResource.DEPTH_INFINITE, getSubMonitor());
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
		jprj.setRawClasspath(newClasspath, getSubMonitor());
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
			f.create(true, true, getSubMonitor());
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
			String pkgName = String.format("%s.%s", parameters.getPackageName(), subPackage);
			ejbPackageFragmentRoot.createPackageFragment(pkgName, true, getSubMonitor());
		}
	}
	

	private void copyFromUrlToFolder(IContainer iContainer, URL content, String fileName) 
	throws IOException, CoreException 
	{
		InputStream in = content.openConnection().getInputStream();
		iContainer.getFile(new Path(fileName)).create(in, true, getSubMonitor());
	}
	

	private IProject openExistingProject(String prjName) 
	throws CoreException 
	{
		IProject prj = parameters.getRoot().getProject(prjName);
		if (!prj.exists()) {
			IStatus status = new Status(
					IStatus.ERROR, 
					Activator.PLUGIN_ID, 
					"Project "+prjName+" should exist but doesn't"
					);
			throw new CoreException(status);
		}
		prj.open(getSubMonitor());
		return prj;
	}
	

	private void advanceProgress(int amountFinished, String nowBeginning) 
	throws InterruptedException 
	{
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

	public boolean isValid() {
		return isValid;
	}
	

	public AzzyztedProjectParameters getParameters() {
		return parameters;
	}
	

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}


}
