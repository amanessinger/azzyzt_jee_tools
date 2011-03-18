package org.azzyzt.jee.runtime.service;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"type", "code", "detail"})
public class ExceptionToSuccessMapper {

	@XmlElement
	public String type = "FAULT";
	@XmlElement
	public String code = "0";
	@XmlElement
	public String detail = null;

	public ExceptionToSuccessMapper() {
		super();
	}

	public Response toResponse(Throwable e) {
		e.printStackTrace();
		detail = e.getClass().getSimpleName();
		return Response.ok().entity(this).type("application/xml").build();
	}

}