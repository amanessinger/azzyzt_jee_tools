/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
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

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.azzyzt.jee.tools.common.Util;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class CxfRestClientProject extends JavaProject {

	public CxfRestClientProject(
			String name, 
			Context context, 
			List<JavaProject> projectsOnBuildPath, List<URL> cxfRestClientJars) 
	throws CoreException 
	{
		super(name, context, Arrays.asList(DWP_SRC_FOLDER_NAME, GENERATED_SRC_FOLDER_NAME));

		addProjectsToBuildPath(projectsOnBuildPath);
		fixFacets(context.getFacets().javaFacet);

		IFolder lib = createFolderForPathIfNeeded(new Path("lib"));
		for (URL jarUrl : cxfRestClientJars) {
			try {
				String path = jarUrl.getPath();
				String fileName = path.substring(path.lastIndexOf('/') + 1);
				copyFromUrlToFolder(lib, jarUrl, fileName);
				addJarToClassPath(new Path("lib/"+fileName));
			} catch (IOException e) {
				throw Util.createCoreException("Can't install CXF REST client libraries", e);
			}
		}
		moveJreToEndOfClassPath();
	}
	
}
