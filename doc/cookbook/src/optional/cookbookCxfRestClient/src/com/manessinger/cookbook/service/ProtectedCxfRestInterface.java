package com.manessinger.cookbook.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path(value="protected")
public interface ProtectedCxfRestInterface {

	@GET
    @Path("helloAdmin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAdmin(@QueryParam(value="s") String s);
	
	@GET
    @Path("helloSeniorAdmin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSeniorAdmin(@QueryParam(value="s") String s);
	
}
