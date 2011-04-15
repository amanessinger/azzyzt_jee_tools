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

/*
 * Code generation makes use of and bundles a copy of StringTemplate, 
 * which is
 * 
 * Copyright (c) 2008, Terence Parr
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the 
 * following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright 
 * notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright 
 * notice, this list of conditions and the following disclaimer in 
 * the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public class JavaGenerator extends SourceGenerator {
	
	private boolean generateFields = true;
	private boolean generateGettersSetters = true;
	private boolean generateDefaultConstructor = true;
	
	public JavaGenerator(MetaModel model, String sourceFolder, String stringTemplateGroup) {
        super(model, sourceFolder, stringTemplateGroup);
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
