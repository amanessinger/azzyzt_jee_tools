package org.azzyzt.jee.runtime.exception;

public class InternalErrorException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	public InternalErrorException(String detailMessage) {
		super("Internal Error", detailMessage);
	}

}
