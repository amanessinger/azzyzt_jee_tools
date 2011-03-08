package org.azzyzt.jee.runtime.exception;

public class InvalidFieldException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	private String invalidFieldValue;
	
	public InvalidFieldException(String invalidFieldValue) {
		super("Access error",
		  	  "Field name is invalid");
		this.setInvalidField(invalidFieldValue);
	}

	public void setInvalidField(String invalidFieldValue) {
		this.invalidFieldValue = invalidFieldValue;
	}

	public String getInvalidField() {
		return invalidFieldValue;
	}
}
