package org.azzyzt.jee.tools.mwe.generator;

import java.io.File;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public abstract class SourceGenerator {

	private MetaModel model;
	private String sourceFolder;
	private ProgressIndicator progress;
	private File absoluteSourceFolder;
	protected String stringTemplateGroup;
	protected Log logger;

	public SourceGenerator(MetaModel model, String sourceFolder, String stringTemplateGroup, Log logger) {
		this.model = model;
		this.stringTemplateGroup = stringTemplateGroup;
		this.logger = logger;
		this.progress = new ProgressIndicator(model); // TODO pass progress indicator in via constructor
		setSourceFolder(sourceFolder);
	}
	
	public abstract int generate();

	protected MetaModel getModel() {
		return model;
	}

	protected void setSourceFolder(String sourceFolder) {
        File sourceFolderDir = new File(sourceFolder);
        if (sourceFolderDir.isDirectory()) {
        	setAbsoluteSourceFolder(sourceFolderDir.getAbsoluteFile());
        	logger.log("Generating in "+getAbsoluteSourceFolder());
        } else {
        	throw new ToolError("Source folder "+sourceFolder+" is no directory");
        }
		this.sourceFolder = sourceFolder;
	}

	protected String getSourceFolder() {
		return sourceFolder;
	}

	protected ProgressIndicator getProgress() {
		return progress;
	}

	public void setAbsoluteSourceFolder(File absoluteSourceFolder) {
		this.absoluteSourceFolder = absoluteSourceFolder;
	}

	public File getAbsoluteSourceFolder() {
		return absoluteSourceFolder;
	}

}