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

package org.azzyzt.jee.tools.mwe.projectgen.workers;

import java.net.URL;

import org.azzyzt.jee.tools.common.Util;
import org.azzyzt.jee.tools.mwe.projectgen.ProjectGen;
import org.azzyzt.jee.tools.project.Context;
import org.azzyzt.jee.tools.project.ProjectUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

public class MWEGeneratorWorker {

	private IProgressMonitor monitor;

	public void callMWEGenerator(IProject prj) 
	throws InterruptedException, CoreException 
	{
		monitor.beginTask("Generating code", 100);
		
		try {
			/*
			 * TODO fix this to use a plugin setting stored with the project
			 */
			String prjName = prj.getName();
			String ejbSuffix = Context.PROJECT_SUFFIX_EJB;
			if (!prjName.endsWith(ejbSuffix)) {
				throw Util.createCoreException(
						"The project name does not end with \""
						+ejbSuffix+"\", can't determine project stem", 
						null
				);
			}
			String projectBaseName = prjName.substring(0, prjName.length() - 3);
			Context context = new Context();
			context.setProjectBaseName(projectBaseName);
			
			URL[] classPathEntries = ProjectUtil.classPathURLsForToolMainClass(prj, ProjectGen.extraURLsForToolMainClass());
			String fqMainClassName = "org.azzyzt.jee.tools.mwe.StandardProjectStructureGenerator";
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			String[] args = {
					root.getLocation()+""+IPath.SEPARATOR,
					projectBaseName
			};

			Util.callExternalMainClass("Generate code from entities", classPathEntries, fqMainClassName, args);
			monitor.worked(60);
			
			for (String name : context.allProjectNames()) {
				IProject project = context.getRoot().getProject(name);
				project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 10));
			}
		} finally {
			monitor.done();
		}
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
}
