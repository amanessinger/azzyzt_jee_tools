package org.azzyzt.jee.tools.project;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.common.Util;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class AzzyztToolsProject extends Project {

	private static final String PROJECT_NAME = "azzyzt_tools";
	
	private static final String DEVELOPMENT_FOLDER_NAME = "DEVELOPMENT";
	private static final String RUNTIME_SUB_FOLDER_NAME = "runtime";
	
	private String azzyztVersion;
	private String versionFolderName;
	private IFolder versionFolderPath;
	
	private URL mweUrl;
	private List<URL> libUrls;
	private URL runtimeUrl;
	private URL runtimeSiteUrl;	
	
	private List<URL> localExtraUrls = new ArrayList<URL>();

	private boolean inDevelopmentMode = Platform.inDevelopmentMode();

	public AzzyztToolsProject(String azzyztVersion, URL mweUrl, List<URL> libUrls, URL runtimeUrl, URL runtimeSiteUrl, Context context) 
	throws CoreException {
		super(PROJECT_NAME, context);
		this.azzyztVersion = azzyztVersion;
		this.mweUrl = mweUrl;
		this.libUrls = libUrls;
		this.runtimeUrl = runtimeUrl;
		this.runtimeSiteUrl = runtimeSiteUrl;
		buildIfNecessary();
	}

	private void buildIfNecessary() 
	throws CoreException {
		if (inDevelopmentMode) {
			versionFolderName = DEVELOPMENT_FOLDER_NAME;
		} else {
			versionFolderName = azzyztVersion;
		}
		versionFolderPath = createFolderForPathIfNeeded(new Path(versionFolderName));
		List<URL> urlsToCopy = new ArrayList<URL>(libUrls);
		urlsToCopy.add(0, mweUrl);
		copyToFolder(urlsToCopy, null);
		
		urlsToCopy.clear();
		urlsToCopy.add(runtimeUrl);
		urlsToCopy.add(runtimeSiteUrl);
		copyToFolder(urlsToCopy, RUNTIME_SUB_FOLDER_NAME);
	}

	private void copyToFolder(List<URL> urlsToCopy, String subfolder) 
	throws CoreException 
	{
		IFolder folderPath;
		if (subfolder == null) {
			folderPath = versionFolderPath;
		} else {
			folderPath = createFolderForPathIfNeeded(new Path(versionFolderName+"/"+subfolder));
		}
		for (URL u : urlsToCopy) {
			String filename = u.toExternalForm();
			filename = filename.substring(filename.lastIndexOf('/') + 1);
			// always copy in development mode, otherwise if it does not exist
			boolean fileExists = folderPath.exists(new Path(filename));
			if (inDevelopmentMode || !fileExists) {
				try {
					if (fileExists) {
						// delete old dev version
						folderPath.findMember(filename).delete(true, getContext().getSubMonitor());
					}
					URL localUrl = copyFromUrlToFolder(folderPath, u, filename);
					localExtraUrls.add(localUrl);
				} catch (IOException e) {
					throw Util.createCoreException("Can't install libraries into EAR project", e);
				}
			}
		}
	}

	public List<URL> extraURLsForToolMainClass() {
		return localExtraUrls;
	}

	
}
