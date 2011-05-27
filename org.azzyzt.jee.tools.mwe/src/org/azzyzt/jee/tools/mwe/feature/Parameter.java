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

package org.azzyzt.jee.tools.mwe.feature;

import java.io.File;
import java.util.List;

import org.azzyzt.jee.tools.mwe.builder.TargetEnumerator;
import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class Parameter {
	
	public static final boolean IS_OPTIONAL = true;
	public static final boolean IS_MANDATORY = false;
	
	private String name;
	private ParameterType type;
	private boolean isOptional;
	private Object value = null;
	
	public Parameter(String name, ParameterType type, boolean isOptional) {
		super();
		this.name = name;
		this.type = type;
		this.isOptional = isOptional;
	}

	public String getName() {
		return name;
	}

	public ParameterType getType() {
		return type;
	}

	public boolean isOptional() {
		return isOptional;
	}

	protected Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		switch (type) {
		case String:
			if (!(value instanceof String)) {
				throw new ToolError("Value for parameter "+name+" must be a string but is a "+value.getClass().getName());
			}
			break;
		case SourceFolder:
			if (!(value instanceof String)) {
				throw new ToolError("Value for parameter "+name+" must be a string but is a "+value.getClass().getName());
			}
			File dir = new File((String) value);
			if (!dir.isDirectory() || ! dir.canWrite()) {
				throw new ToolError("Value for parameter "+name+" must be the name of a writable directory but is "+value);
			}
			break;
		case ListString:
			if (!(value instanceof List<?>)) {
				throw new ToolError("Value for parameter "+name+" must be a list of strings but is a "+value.getClass().getName());
			}
			break;
		case TargetEnumerator:
			if (!(value instanceof TargetEnumerator)) {
				throw new ToolError("Value for parameter "+name+" must be a TargetEnumerator but is a "+value.getClass().getName());
			}
			break;
		default:
			throw new ToolError("Unsupported parameter type "+type);
		}
		this.value = value;
	}

}
