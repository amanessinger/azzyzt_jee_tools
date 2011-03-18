package org.azzyzt.jee.runtime.exception;

public class AccessDeniedException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
		super("Access denied",
			  "The access to a restricted resource has been denied");
	}

}
