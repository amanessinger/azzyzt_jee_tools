package com.manessinger.cookbook.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.azzyzt.jee.runtime.annotation.RequiresCredentials;

import com.manessinger.cookbook.meta.EJBInterceptor;


/**
 * A bean for testing credential-based authorization. It's via ProtectedDelegator
 * from CookbookReatTest
 */
@LocalBean
@Stateless
@WebService(serviceName="cookbook")
@RequiresCredentials("admin")
@Interceptors(EJBInterceptor.class)
public class ProtectedBean {

    public String helloAdmin(String s) {
        return s;
    }
    
    @RequiresCredentials("senior(rank=2)")
    public String helloSeniorAdmin(String s) {
        return s;
    }
}