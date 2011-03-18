package org.azzyzt.jee.runtime.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends TranslatableException {
 
    private static final long serialVersionUID = 1L;
 
    private List<ViolationDetail> details = new ArrayList<ViolationDetail>();
 
    public ValidationException(List<ViolationDetail> details) { 
		super("Validation failed",
	  	      "A parameter has an invalid value");
    	this.details = details;
    }
 
    public List<ViolationDetail> getViolationDetail() { return details; }
}
