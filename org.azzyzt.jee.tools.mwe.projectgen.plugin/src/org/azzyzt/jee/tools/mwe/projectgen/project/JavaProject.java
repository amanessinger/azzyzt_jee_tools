package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.azzyzt.jee.tools.mwe.projectgen.workers.Util;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class JavaProject extends Project {
	
	public static final String GENERATED_SRC_FOLDER_NAME = "generated";
	public static final String DWP_SRC_FOLDER_NAME = "src";

	private IJavaProject jp;
	private List<String> sourceFolderNames;
	
	protected JavaProject() { }
	
	private static IJavaProject asIJavaProject(Project p) throws CoreException {
		return (IJavaProject)p.getP().getNature(JavaCore.NATURE_ID);
	}

	protected static JavaProject asJavaProject(Project p) throws CoreException {
		JavaProject jp = new JavaProject();
		jp.setP(p.getP());
		jp.setFp(p.getFp());
		jp.setName(p.getName());
		jp.setContext(p.getContext());
		jp.setJp(asIJavaProject(p));
		return jp;
	}
	
	public JavaProject(String name, Context context, List<String> sourceFolderNames) 
	throws CoreException 
	{
		super(name, context);
		this.sourceFolderNames = sourceFolderNames;
		installJavaFacet();
		jp = asIJavaProject(this);		
	}

	protected void installJavaFacet()
	throws CoreException 
	{
		JavaFacetInstallConfig config = (JavaFacetInstallConfig) Project.createConfigObject(getContext().getFacets().javaFacetVersion);
		List<IPath> sourceFolderPaths = new ArrayList<IPath>();
		for (String sourceFolderName : sourceFolderNames) {
			sourceFolderPaths.add((IPath) new Path(sourceFolderName));
		}
		config.setSourceFolders(sourceFolderPaths);
		
		installFacet(getContext().getFacets().javaFacetVersion, config);
	}
	
	protected void installJpaFacet() 
	throws CoreException 
	{
		IDataModel config = (IDataModel) Project.createConfigObject(getContext().getFacets().jpaFacetVersion);
		
		installFacet(getContext().getFacets().jpaFacetVersion, config);

		/*
		 *  TODO make sure we get the highest EclipseLink 2.1 ???
		 *  Sometimes we get it, sometimes we just get Generic 2.0. Find out why.
		 */
		//JpaProject jpaProject = JptCorePlugin.getJpaProject(getP());
		//JptCorePlugin.setJpaPlatformId(getP(), "eclipselink2_1");
		//jpaProject.update();
	}

	protected void addProjectsToBuildPath(List<JavaProject> projectsOnBuildPath) 
	throws CoreException 
	{	
		if (projectsOnBuildPath == null) return;
		
		for (JavaProject p : projectsOnBuildPath) {
			Util.appendProjectToClassPath(jp, p.getJp());
		}
	}

	protected void addFolderToClassPath(Path path)
	throws CoreException, JavaModelException 
	{
		IClasspathEntry classpathEntry = JavaCore.newSourceEntry(getP().getFullPath().append(path));
		IClasspathEntry[] rawClasspath = jp.getRawClasspath();
		IClasspathEntry[] newClasspath = new IClasspathEntry[rawClasspath.length + 1];
		newClasspath[0] = classpathEntry;
		System.arraycopy(rawClasspath, 0, newClasspath, 1, rawClasspath.length);
		jp.setRawClasspath(newClasspath, getContext().getSubMonitor());
	}

	protected void createSourceFolderIfNeededAndAddToProject(String folderName) 
	throws CoreException, JavaModelException 
	{
		addFolderToClassPath(createFolderPathIfNeeded(folderName));
	}
	
	protected void createSubpackages(String srcFolderName, String...pkgNames) 
	throws CoreException, JavaModelException 
	{
		IPath absEjbSrcPath = getP().getFolder(srcFolderName).getFullPath();
		IPackageFragmentRoot ejbPackageFragmentRoot = jp.findPackageFragmentRoot(absEjbSrcPath);
		for (String subPackage : Arrays.asList(pkgNames)) {
			String pkgName = String.format("%s.%s", getContext().getPackageName(), subPackage);
			ejbPackageFragmentRoot.createPackageFragment(pkgName, true, getContext().getSubMonitor());
		}
	}

	protected void buildJavaClass(
			String srcFolderName, 
			String pkgName, 
			String fqBuilder, 
			String jobTitle) 
	throws InterruptedException, CoreException 
	{
		IFolder srcFolder = getP().getFolder(srcFolderName);
		IPath srcFolderPath = srcFolder.getLocation();
		
		URL[] classPathEntries = Util.classPathURLsForToolMainClass(getP());
		String fqMainClassName = "org.azzyzt.jee.tools.mwe.GenericGenerator";
		String[] args = {
				srcFolderPath.toString(), 
				pkgName, 
				fqBuilder
		};

		Util.callExternalMainClass(jobTitle, classPathEntries, fqMainClassName, args);		
	}

	public void setJp(IJavaProject jp) {
		this.jp = jp;
	}

	public IJavaProject getJp() {
		return jp;
	}
	
	public List<String> getSourceFolderNames() {
		return sourceFolderNames;
	}

}
