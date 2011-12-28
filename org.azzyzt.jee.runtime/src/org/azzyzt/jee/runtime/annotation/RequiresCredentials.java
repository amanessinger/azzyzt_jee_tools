package org.azzyzt.jee.runtime.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that can be put on whole service bean classes
 * and/or on service methods. Its value is a string representation
 * of the credentials required to call any method of an annotated
 * service bean, respectively an annotated method.
 * 
 * @see org.azzyzt.jee.runtime.meta.Credentials
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface RequiresCredentials {
	
	String value() default "";

}
