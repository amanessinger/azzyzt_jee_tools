package org.azzyzt.jee.runtime.exception;

public class QuerySyntaxException extends TranslatableException {

	private static final long serialVersionUID = 1L;

	private String description;
	
	public QuerySyntaxException(String description) {
		super("Access error",
		  	  "Query is syntactically incorrect");
		this.setDescription(description);
	}

	public QuerySyntaxException(String description, Throwable t) {
		super("Access error",
	  	  	  "Query is syntactically incorrect",
	  	  	  t);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
