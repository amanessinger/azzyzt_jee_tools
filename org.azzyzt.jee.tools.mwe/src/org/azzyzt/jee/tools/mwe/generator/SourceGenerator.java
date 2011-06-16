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

package org.azzyzt.jee.tools.mwe.generator;

import java.io.File;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public abstract class SourceGenerator {

	private MetaModel model;
	private MetaModel masterModel;
	private String sourceFolder;
	private File absoluteSourceFolder;
	protected String stringTemplateGroup;

	public SourceGenerator(MetaModel model, MetaModel masterModel, String sourceFolder, String stringTemplateGroup) {
		this.model = model;
		this.masterModel = masterModel;
		this.stringTemplateGroup = stringTemplateGroup;
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
        } else {
        	throw new ToolError("Source folder "+sourceFolder+" is no directory");
        }
		this.sourceFolder = sourceFolder;
	}

	protected String getSourceFolder() {
		return sourceFolder;
	}

	public void setAbsoluteSourceFolder(File absoluteSourceFolder) {
		this.absoluteSourceFolder = absoluteSourceFolder;
	}

	public File getAbsoluteSourceFolder() {
		return absoluteSourceFolder;
	}

	public MetaModel getMasterModel() {
		return masterModel;
	}

}