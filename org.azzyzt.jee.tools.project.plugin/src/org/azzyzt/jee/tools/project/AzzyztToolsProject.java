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
	
	private String azzyztVersion;
	private IFolder versionFolderPath;
	private URL mweUrl;
	private List<URL> libUrls;

	private boolean inDevelopmentMode = Platform.inDevelopmentMode();
	
	public AzzyztToolsProject(String azzyztVersion, URL mweUrl, List<URL> libUrls, Context context) 
	throws CoreException {
		super(PROJECT_NAME, context);
		this.azzyztVersion = azzyztVersion;
		this.mweUrl = mweUrl;
		this.libUrls = libUrls;
		buildIfNecessary();
	}

	private void buildIfNecessary() 
	throws CoreException {
		String versionFolderName;
		if (inDevelopmentMode) {
			versionFolderName = DEVELOPMENT_FOLDER_NAME;
		} else {
			versionFolderName = azzyztVersion;
		}
		versionFolderPath = createFolderForPathIfNeeded(new Path(versionFolderName));
		List<URL> urlsToCopy = new ArrayList<URL>(libUrls);
		urlsToCopy.add(0, mweUrl);
		for (URL u : urlsToCopy) {
			String filename = u.toExternalForm();
			filename = filename.substring(filename.lastIndexOf('/') + 1);
			// always copy in development mode, otherwise if it does not exist
			boolean fileExists = versionFolderPath.exists(new Path(filename));
			if (inDevelopmentMode || !fileExists) {
				try {
					if (fileExists) {
						// delete old dev version
						versionFolderPath.findMember(filename).delete(true, getContext().getSubMonitor());
					}
					copyFromUrlToFolder(versionFolderPath, u, filename);
					System.err.println("Copied "+filename+" to "+versionFolderPath);
				} catch (IOException e) {
					throw Util.createCoreException("Can't install runtime libraries into EAR project", e);
				}
			}
		}
	}

	
}
