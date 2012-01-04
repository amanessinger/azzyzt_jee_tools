package com.manessinger.cookbook.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.azzyzt.jee.runtime.service.RESTDelegatorBase;


/**
 * A rest delegator called by CookbookRestTest for testing 
 * credential-based authorization.
 */
@Stateless
@Path(value="protected")
@Interceptors(RESTInterceptor.class)
public class ProtectedDelegator extends RESTDelegatorBase {

    @EJB
    ProtectedBean svcBean;

    public ProtectedDelegator() { super(); }

    @GET
    @Path("helloAdmin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAdmin(@QueryParam(value="s") String s)
    {
        nocache();
        return svcBean.helloAdmin(s);
    }

    @GET
    @Path("helloSeniorAdmin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSeniorAdmin(@QueryParam(value="s") String s)
    {
        nocache();
        return svcBean.helloSeniorAdmin(s);
    }

}