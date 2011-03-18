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
