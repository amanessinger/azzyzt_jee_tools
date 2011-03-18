package org.azzyzt.jee.runtime.exception;

public class NotYetImplementedException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	public NotYetImplementedException() {
		super("Not yet implemented",
			  "The requested operation has not yet been implemented");
	}

}
