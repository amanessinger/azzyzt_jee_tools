package org.azzyzt.jee.tools.mwe.model.annotation;

import java.util.List;

import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public interface MetaAnnotatable {
	
	public void setMetaAnnotationInstances(List<MetaAnnotationInstance> instances);
	
	public void addMetaAnnotationInstance(MetaAnnotationInstance instance);
	
	public List<MetaAnnotationInstance> getMetaAnnotationInstances();
	
	public void addReferencedForeignType(MetaType referencedForeignType);

}
