package org.azzyzt.jee.runtime.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface AzzyztGeneratorOptions {
	
	AzzyztGeneratorCutback[] cutbacks() default {};

}
