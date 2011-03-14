package org.azzyzt.jee.tools.mwe.projectgen.project;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.azzyzt.jee.tools.mwe.projectgen.Activator;
import org.azzyzt.jee.tools.mwe.projectgen.util.Util;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
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

public class EarProject extends Project {

	/*
	 *  defined but not accessible in 
	 *  org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent.LIBARCHIVETYPE
	 */
	private static final String LIBARCHIVETYPE = "lib";

	public static EarProject create(String name, Context context, Project...projects) 
	throws CoreException 
	{
		EarProject ear = new EarProject(name, context);
		
		ear.installEARFacet(projects);
		ear.installServerSpecificFacets();
		ear.fixFacets(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_FACET);
		ear.installRuntimeLibsIntoEar();
		
		return ear;
	}
	
	private EarProject(String name, Context context) 
	throws CoreException 
	{
		super(name, context);
	}

	private void installEARFacet(Project... projects) throws CoreException {
		IProject[] iProjects = new IProject[0];
		if (projects != null) {
			iProjects = new IProject[projects.length];
			for (int i = 0; i < projects.length; i++) {
				iProjects[i] = projects[i].getP();
			}
		}
		IDataModel config = (IDataModel) createConfigObject(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_60);
		
		config.setProperty(
				IEarFacetInstallDataModelProperties.J2EE_PROJECTS_LIST, 
				Arrays.asList(iProjects)
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
	
		installFacet(IJ2EEFacetConstants.ENTERPRISE_APPLICATION_60, config);
	}

	private void installRuntimeLibsIntoEar() 
	throws CoreException 
	{
		IFolder lib = createFolderForPathIfNeeded(new Path("lib"));
		
		try {
			copyFromUrlToFolder(lib, Activator.getJeeRuntimeJarUrl(), Activator.JEE_RUNTIME_JAR);
			copyFromUrlToFolder(lib, Activator.getJeeRuntimeSiteJarUrl(), Activator.JEE_RUNTIME_SITE_JAR);
		} catch (IOException e) {
			throw Util.createCoreException("Can't install runtime libraries into EAR project", e);
		}
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

		handlePrfx = LIBARCHIVETYPE + IPath.SEPARATOR
				+ getP().getName() + IPath.SEPARATOR + "lib" + IPath.SEPARATOR;
		jarCmp = ComponentCore.createArchiveComponent(getP(), handlePrfx + Activator.JEE_RUNTIME_JAR);
		jarRef = ComponentCore.createReference(earCmp, jarCmp, new Path("/lib"));
		if (!references.contains(jarRef)) {
			references.add(jarRef);
		}
		
		handlePrfx = LIBARCHIVETYPE + IPath.SEPARATOR
				+ getP().getName() + IPath.SEPARATOR + "lib" + IPath.SEPARATOR;
		jarCmp = ComponentCore.createArchiveComponent(getP(), handlePrfx + Activator.JEE_RUNTIME_SITE_JAR);
		jarRef = ComponentCore.createReference(earCmp, jarCmp, new Path("/lib"));
		if (!references.contains(jarRef)) {
			references.add(jarRef);
		}

		earCmp.setReferences(references.toArray(new IVirtualReference[references.size()]));
	}

	private void copyFromUrlToFolder(IContainer iContainer, URL content, String fileName) 
	throws IOException, CoreException 
	{
		InputStream in = content.openConnection().getInputStream();
		iContainer.getFile(new Path(fileName)).create(in, true, getContext().getSubMonitor());
	}
	
}
