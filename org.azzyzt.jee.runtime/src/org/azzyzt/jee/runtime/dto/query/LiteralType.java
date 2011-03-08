package org.azzyzt.jee.runtime.dto.query;

public enum LiteralType {
	
	STRING(String.class), 
	SHORT(Short.class), 
	INTEGER(Integer.class), 
	LONG(Long.class), 
	FLOAT(Float.class),
	DOUBLE(Double.class),
	;
	
	private final Class<?> clazz;

	LiteralType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> clazz() { return clazz; }
}
