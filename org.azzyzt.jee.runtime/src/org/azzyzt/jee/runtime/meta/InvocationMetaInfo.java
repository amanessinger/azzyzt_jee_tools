package org.azzyzt.jee.runtime.meta;

import java.util.Date;

public class InvocationMetaInfo {

	private Date started;
	
	private String authenticatedUserName;
	
	public InvocationMetaInfo() {
		this.started = new Date();
	}

	public Date getStarted() {
		return started;
	}

	public String getAuthenticatedUserName() {
		return authenticatedUserName;
	}

	public void setAuthenticatedUserName(String authenticatedUserName) {
		this.authenticatedUserName = authenticatedUserName;
	}

}
