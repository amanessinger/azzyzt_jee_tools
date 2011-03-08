package org.azzyzt.jee.runtime.exception;

public class EntityInstantiationException extends TranslatableException {

    private static final long serialVersionUID = 1L;
    
    private String className;
    
    public EntityInstantiationException(Class<?> clazz) {
        super("Access error",
  			  "An object could not be instantiated");
        this.className = clazz.getSimpleName();
        setContext("An entity of class "+clazz.getSimpleName()+" could not be instantiated");
    }

    public String getClassName() { return className; }
}
