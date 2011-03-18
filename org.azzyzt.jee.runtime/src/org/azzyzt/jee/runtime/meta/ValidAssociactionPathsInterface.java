package org.azzyzt.jee.runtime.meta;


public interface ValidAssociactionPathsInterface {

	public boolean contains(Class<?> clazz, String path);
	
	public AssociationPathInfo get(Class<?> clazz, String path);

	public void add(Class<?> clazz, String path, AssociationPathInfo api);
}