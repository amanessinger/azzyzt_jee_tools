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

package org.azzyzt.jee.runtime.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String message = "Internal Error";
	private String type = "";
	private String source = "";
	private String presentationType = "Internal Error";
	private String presentationMessage = "An internal error has occurred";
	private String context = "";

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPresentationType() {
		return presentationType;
	}
	public void setPresentationType(String presentationType) {
		this.presentationType = presentationType;
	}
	public String getPresentationMessage() {
		return presentationMessage;
	}
	public void setPresentationMessage(String presentationMessage) {
		this.presentationMessage = presentationMessage;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

}
