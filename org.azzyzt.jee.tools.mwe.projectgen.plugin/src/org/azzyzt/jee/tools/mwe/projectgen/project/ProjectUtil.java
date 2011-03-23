package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.common.Util;
import org.azzyzt.jee.tools.mwe.projectgen.Activator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class ProjectUtil {

	public static IJavaProject getJavaProject(IProject prj) throws CoreException {
		IJavaProject jprj = (IJavaProject)prj.getNature(JavaCore.NATURE_ID);
		return jprj;
	}

	public static List<URL> computeClassPathForProject(IJavaProject jprj) throws CoreException {
		String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(jprj);
		
		List<URL> classPathUrls = new ArrayList<URL>(classPath.length * 2);
		
		for (String s : classPath) {
			try {
				URL url = new File(s).toURI().toURL();
				classPathUrls.add(url);
			} catch (MalformedURLException e) {
				throw Util.createCoreException("Can't compute class path for project "+jprj.getProject().getName(), e);
			}
		}
		return classPathUrls;
	}

	public static List<URL> computeClassPathForProject(IProject prj) throws CoreException {
		IJavaProject jprj = getJavaProject(prj);
		return computeClassPathForProject(jprj);
	}

	public static URL[] classPathURLsForToolMainClass(IProject prj)
			throws CoreException {
		List<URL> classPathUrls = computeClassPathForProject(prj);
		classPathUrls.add(Activator.getJeeToolsMweJarUrl());
		classPathUrls.add(Activator.getJeeRuntimeJarUrl());
		classPathUrls.add(Activator.getJeeRuntimeSiteJarUrl());
		classPathUrls.addAll(Activator.getToolsLibJarUrls());
	
		URL[] classPathEntries = classPathUrls.toArray(new URL[0]);
		return classPathEntries;
	}

	public static void appendProjectToClassPath(IJavaProject jprj, IJavaProject jprjAdded) 
	throws CoreException 
	{
		try {
			IClasspathEntry[] entries = jprj.getRawClasspath();
			IClasspathEntry newEntry = JavaCore.newProjectEntry(jprjAdded.getPath(), true);
			for (IClasspathEntry entry : entries) {
				if (newEntry.equals(entry)) {
					return;
				}
			}
			IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
			System.arraycopy(entries, 0, newEntries, 0, entries.length);
			newEntries[newEntries.length - 1] = newEntry;
			jprj.setRawClasspath(newEntries, null);
		} catch (JavaModelException e) {
			throw Util.createCoreException(
					"Can't add "+jprjAdded.getProject().getName()
					+" to class path of "+jprj.getProject().getName(), e
			);
		}
	}

}
