package org.azzyzt.jee.runtime.exception;

public class EntityNotFoundException extends TranslatableException {

    private static final long serialVersionUID = 1L;
    
    private String className;
    private String idValue;
    
    public EntityNotFoundException(Class<?> clazz, String idValue) {
        super("Access error",
  			  "Object not found");
        this.className = clazz.getSimpleName();
        this.idValue = idValue;
        setContext("Entity of class "+clazz.getSimpleName()+" with id "+idValue+" not found");
    }

    public String getClassName() { return className; }
    public String getId() { return idValue; }
}
