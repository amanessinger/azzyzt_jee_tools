package org.azzyzt.jee.tools.mwe.generator;

import java.io.File;
import java.util.logging.Logger;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public abstract class SourceGenerator {

	public static Logger logger = Logger.getLogger(SourceGenerator.class.getPackage().getName());

	private MetaModel model;
	private String sourceFolder;
	private ProgressIndicator progress;
	private File absoluteSourceFolder;
	protected String stringTemplateGroup;

	public SourceGenerator(MetaModel model, String sourceFolder, String stringTemplateGroup) {
		this.model = model;
		setSourceFolder(sourceFolder);
		this.stringTemplateGroup = stringTemplateGroup;
		
		this.progress = new ProgressIndicator(model); // TODO pass progress indicator in via constructor
	}
	
	public abstract int generate();

	protected MetaModel getModel() {
		return model;
	}

	protected void setSourceFolder(String sourceFolder) {
        File sourceFolderDir = new File(sourceFolder);
        if (sourceFolderDir.isDirectory()) {
        	setAbsoluteSourceFolder(sourceFolderDir.getAbsoluteFile());
        	logger.info("Generating in "+getAbsoluteSourceFolder());
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