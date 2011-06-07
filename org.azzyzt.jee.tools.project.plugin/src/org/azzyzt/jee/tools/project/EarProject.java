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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.azzyzt.jee.tools.common.Common;
import org.azzyzt.jee.tools.common.Util;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.earcreation.IEarFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.internal.project.facet.EARFacetProjectCreationDataModelProvider;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.FacetProjectCreationDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class EarProject extends FacetedProject {

	private static final String EAR_CONTENT = "EarContent";
	private static final String EAR_CONTENT_META_INF = EAR_CONTENT+"/META-INF";
	private static final String AZZYZT_PROPERTIES_FILE = EAR_CONTENT_META_INF+"/azzyzt.xml";
	
	/*
	 *  defined but not accessible in 
	 *  org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent.LIBARCHIVETYPE
	 */
	private static final String LIBARCHIVETYPE = "lib";
	
	private AzzyztProperties azzyztProperties = null;
	private boolean isRuntimeLibraryOutOfDate = false;

	public EarProject(String azzyztVersion, String name, Context context, Map<String, URL> runtimeJars) 
	throws CoreException 
	{
		super(name, context);
		
		ensureAzzyztProperties(azzyztVersion);
		
		if (isNewlyCreated()) {
			installEARFacet();
			installServerSpecificFacets();
			fixFacets(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_FACET);
		}
		
		if (isRuntimeLibraryOutOfDate()) {
			installRuntimeLibsIntoEar(runtimeJars);
		}
	}

	private void installEARFacet() 
	throws CoreException 
	{
		IDataModel config = (IDataModel) createConfigObject(getContext().getFacets().earFacetVersion);
		
		config.setProperty(
				IEarFacetInstallDataModelProperties.J2EE_PROJECTS_LIST, 
				Collections.EMPTY_LIST
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
	
		installFacet(getContext().getFacets().earFacetVersion, config);
	}

	private void installRuntimeLibsIntoEar(Map<String, URL> runtimeJars) 
	throws CoreException 
	{
		IFolder lib = createFolderForPathIfNeeded(new Path("lib"));
		
		IVirtualComponent earCmp = ComponentCore.createComponent(getP());
		
		// get current references
		List<IVirtualReference> references = new ArrayList<IVirtualReference>();
		IVirtualReference[] currentReferences = earCmp.getReferences();
		for (IVirtualReference reference : currentReferences) {
			references.add(reference);
		}
		
		String handlePrfx;
		IVirtualComponent jarCmp;
		IVirtualReference jarRef;

		for (String fileName : runtimeJars.keySet()) {
			URL jarUrl = runtimeJars.get(fileName);
			try {
				copyFromUrlToFolder(lib, jarUrl, fileName);
			} catch (IOException e) {
				throw Util.createCoreException("Can't install runtime libraries into EAR project", e);
			}
			handlePrfx = LIBARCHIVETYPE + IPath.SEPARATOR
					+ getP().getName() + IPath.SEPARATOR + "lib" + IPath.SEPARATOR;
			jarCmp = ComponentCore.createArchiveComponent(getP(), handlePrfx + fileName);
			jarRef = ComponentCore.createReference(earCmp, jarCmp, new Path("/lib"));
			if (!references.contains(jarRef)) {
				references.add(jarRef);
			}
		}

		earCmp.setReferences(references.toArray(new IVirtualReference[references.size()]));
	}

	public void ensureAzzyztProperties(String azzyztVersion) 
	throws CoreException 
	{
		createFolderForPathIfNeeded(new Path(EAR_CONTENT));
		createFolderForPathIfNeeded(new Path(EAR_CONTENT_META_INF));
		File azzyztPropertiesFile = new File(
				getP().getLocationURI().getPath() + "/" + 
				AZZYZT_PROPERTIES_FILE
		);
		
		if (!azzyztPropertiesFile.exists()) {
			/*
			 *  It's either new or a legacy project, in any case we install 
			 *  new libraries and set the version to current
			 */
			isRuntimeLibraryOutOfDate = true;
			Common.getDefault().log("No azzyztPropertiesFile, assuming libraries out of date");
			
			azzyztProperties = new AzzyztProperties(azzyztVersion);
			savePropertiesToFile(azzyztPropertiesFile);
		} else {
			readPropertiesFromFile(azzyztPropertiesFile);
			String versionFound = azzyztProperties.getVersion();
			if (!versionFound.equals(azzyztVersion)) {
				/*
				 * TODO This is a forced, automatic upgrade. Give choice to user.
				 */
				isRuntimeLibraryOutOfDate = true;
				Common.getDefault().log("versionFound ("+versionFound+") != azzyztVersion ("+azzyztVersion+"), assuming libraries out of date");
				azzyztProperties.setVersion(azzyztVersion);
				savePropertiesToFile(azzyztPropertiesFile);
			} else {
				Common.getDefault().log("versionFound ("+versionFound+") == azzyztVersion ("+azzyztVersion+"), assuming libraries are current");
			}
		}
	}

	protected void savePropertiesToFile(File azzyztPropertiesFile)
	throws CoreException 
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(azzyztPropertiesFile));
			writer.write(azzyztProperties.toXml());
			writer.close();
			refresh();
		} catch (IOException e) {
			throw Util.createCoreException("Can't create "+azzyztPropertiesFile, e);
		}
	}
	
	protected void readPropertiesFromFile(File azzyztPropertiesFile)
	throws CoreException 
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(azzyztPropertiesFile));
			StringBuffer xmlBuffer = new StringBuffer();
			String s;
			while ((s = reader.readLine()) != null) {
				xmlBuffer.append(s);
				xmlBuffer.append('\n');
			}
			azzyztProperties = AzzyztProperties.fromXml(xmlBuffer.toString());
		} catch (Exception e) {
			throw Util.createCoreException("Can't read/parse "+azzyztPropertiesFile, e);
		}
	}

	public String createdWithAzzyztVersion() {
		return azzyztProperties.getVersion();
	}

	public boolean isRuntimeLibraryOutOfDate() {
		return isRuntimeLibraryOutOfDate;
	}
}

