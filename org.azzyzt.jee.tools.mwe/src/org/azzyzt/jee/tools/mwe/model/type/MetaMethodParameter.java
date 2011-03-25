/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
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

package org.azzyzt.jee.tools.mwe.model.type;

import java.util.Properties;

public class MetaMethodParameter {

	private String name;
	private MetaType type;
    private MetaModifiers modifiers; // TODO find out how to reflect param modifiers
	private Properties properties = new Properties(); // may be set by a synthesizing builder

	// TODO Introduce MetaAnnotations for parameters. We'll need to link back to the method though 
	
	public MetaMethodParameter(String name, MetaType type, int reflected) {
		super();
		initCommon(name, type);
		this.modifiers = new MetaModifiers(reflected);
	}

	public MetaMethodParameter(String name, MetaType type) {
		super();
		initCommon(name, type);
		this.modifiers = new MetaModifiers();
	}

	private void initCommon(String name, MetaType type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaType getType() {
		return type;
	}

	public void setType(MetaType type) {
		this.type = type;
	}

	public MetaModifiers getModifiers() {
		return modifiers;
	}

	public void setFinal() {
		this.modifiers.setFinal(true);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

}
