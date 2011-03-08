package org.azzyzt.jee.runtime.service;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

public abstract class RESTDelegatorBase {
	
	@Context private HttpServletResponse response;
	@Context private HttpHeaders httpHeaders;
	
	public void nocache() {
		response.addHeader("Cache-Control", "no-store,max-age=0,must-revalidate");
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

}
