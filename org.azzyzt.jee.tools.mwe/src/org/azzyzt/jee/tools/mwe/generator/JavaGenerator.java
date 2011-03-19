package org.azzyzt.jee.tools.mwe.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplateGroupLoader;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaInterface;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public class JavaGenerator extends SourceGenerator {
	
	private boolean generateFields = true;
	private boolean generateGettersSetters = true;
	private boolean generateDefaultConstructor = true;
	
	public JavaGenerator(MetaModel model, String sourceFolder, String stringTemplateGroup, Log logger) {
        super(model, sourceFolder, stringTemplateGroup, logger);
    }

	@Override
    public int generate() {
		int numberOfSourcesGenerated = 0;
        for (MetaDeclaredType mdt : getModel().getTargetMetaDeclaredTypes()) {
        	String dtSource = null;
        	if (mdt instanceof MetaClass) {
				dtSource = generateSourceFileContent(mdt, "class");
        	} else {
        		assert mdt instanceof MetaInterface;
				dtSource = generateSourceFileContent(mdt, "interface");
        	}
        	
        	writeTargetFile(mdt, dtSource);
        	
        	numberOfSourcesGenerated++;
        }
        return numberOfSourcesGenerated;
    }
    
    private void writeTargetFile(MetaDeclaredType mdt, String content) {
		File dir = getAbsoluteSourceFolder();
		String[] packageParts = mdt.getPackageName().split("\\.");
		String filePart = mdt.getSimpleName()+".java";

		for (String packagePart : packageParts) {
			dir = new File(dir.getAbsolutePath()+"/"+packagePart);
			if (dir.exists() && !dir.isDirectory()) {
				throw new ToolError(dir.getAbsolutePath()+" exists but is no directory");
			}
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new ToolError("Can't create directory "+dir.getAbsolutePath());
				}
			}
		}
		File targetFile = new File(dir.getAbsolutePath()+"/"+filePart);
		if (targetFile.exists()) {
			if (!targetFile.delete()) {
				throw new ToolError("Can't delete old file "+targetFile.getAbsolutePath());
			}
		}
		FileWriter writer = null;
		try {
			targetFile.createNewFile(); // FindBugs RV_RETURN_VALUE_IGNORED_BAD_PRACTICE OK
			writer = new FileWriter(targetFile);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new ToolError(e);
		}
	}

	public String generateSourceFileContent(MetaDeclaredType mdt, String artifactType) {
    	String templateDirs = "templates/java";
    	StringTemplateGroupLoader loader = new CommonGroupLoader(templateDirs.toString(), null);
    	StringTemplateGroup.registerGroupLoader(loader);
    	StringTemplateGroup javaGroup = StringTemplateGroup.loadGroup(getStringTemplateGroup());
    	StringTemplate st = javaGroup.getInstanceOf("java"+StringUtils.ucFirst(artifactType));
    	st.setAttribute("mdt", mdt);
    	st.setAttribute("generator", this);
    	return st.toString();
    }

	public String getStringTemplateGroup() {
		return stringTemplateGroup;
	}

	public void setStringTemplateGroup(String stringTemplateGroup) {
		this.stringTemplateGroup = stringTemplateGroup;
	}

	public boolean isGenerateFields() {
		return generateFields;
	}

	public void setGenerateFields(boolean generateFields) {
		this.generateFields = generateFields;
	}

	public boolean isGenerateGettersSetters() {
		return generateGettersSetters;
	}

	public void setGenerateGettersSetters(boolean generateGettersSetters) {
		this.generateGettersSetters = generateGettersSetters;
	}
	
	public boolean isGenerateDefaultConstructor() {
		return generateDefaultConstructor;
	}

	public void setGenerateDefaultConstructor(boolean generateDefaultConstructor) {
		this.generateDefaultConstructor = generateDefaultConstructor;
	}
	
}
