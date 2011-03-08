package org.azzyzt.jee.runtime.exception;

public abstract class TranslatableException extends Exception {

	private static final long serialVersionUID = 1L;

	private String presentationType;
	private String presentationMessage;
	private String context;
	
	public TranslatableException(
			String presentationType,
			String presentationMessage 
	) 
	{
		super();
		this.presentationType = presentationType;
		this.presentationMessage = presentationMessage;
	}

	public TranslatableException(
			String presentationType,
			String presentationMessage,
			Throwable t
	) 
	{
		super(t);
		this.presentationType = presentationType;
		this.presentationMessage = presentationMessage;
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
