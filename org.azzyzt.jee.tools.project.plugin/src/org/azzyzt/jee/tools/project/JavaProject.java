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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.azzyzt.jee.tools.common.Common;
import org.azzyzt.jee.tools.common.Util;
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
			ProjectUtil.appendProjectToClassPath(jp, p.getJp());
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

	protected void moveJreToEndOfClassPath() 
	throws JavaModelException 
	{
		IClasspathEntry[] rawClasspath = jp.getRawClasspath();
		IClasspathEntry[] newRawClassPath = new IClasspathEntry[rawClasspath.length];
		for (int i = 0, offset = 0; i < rawClasspath.length; i++) {
			IClasspathEntry cpe = rawClasspath[i];
			if (cpe.getEntryKind() == IClasspathEntry.CPE_CONTAINER
					&& cpe.getPath().toString().startsWith("org.eclipse.jdt.launching.JRE_CONTAINER")) {
				if (newRawClassPath[rawClasspath.length - 1] == null) {
					newRawClassPath[rawClasspath.length - 1] = cpe;
					offset = 1;
				} else {
					Common.getDefault().log(
							"Project "+getP().getName()+" has more than one JRE class path entry!!"
					);
					newRawClassPath[i - offset] = cpe;
				}
			} else {
				newRawClassPath[i - offset] = cpe;
			}
		}
		jp.setRawClasspath(newRawClassPath, null);
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
			String jobTitle,
			List<URL> extraUrls) 
	throws InterruptedException, CoreException 
	{
		IFolder srcFolder = getP().getFolder(srcFolderName);
		IPath srcFolderPath = srcFolder.getLocation();
		
		URL[] classPathEntries = ProjectUtil.classPathURLsForToolMainClass(getP(), extraUrls);
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
