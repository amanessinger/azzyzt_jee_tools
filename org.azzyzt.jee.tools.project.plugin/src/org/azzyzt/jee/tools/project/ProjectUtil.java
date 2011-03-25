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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.common.Util;
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

	public static URL[] classPathURLsForToolMainClass(IProject prj, List<URL> extraUrls)
			throws CoreException {
		List<URL> classPathUrls = computeClassPathForProject(prj);
		classPathUrls.addAll(extraUrls);
	
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
