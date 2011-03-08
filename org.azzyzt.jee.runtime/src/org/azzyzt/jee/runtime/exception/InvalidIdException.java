package org.azzyzt.jee.runtime.exception;

public class InvalidIdException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	private String invalidIdValue;
	
	public InvalidIdException(String invalidIdValue) {
		super("Access error",
		  	  "Key is invalid");
		this.setInvalidId(invalidIdValue);
	}

	public void setInvalidId(String invalidId) {
		this.invalidIdValue = invalidId;
	}

	public String getInvalidId() {
		return invalidIdValue;
	}
}
