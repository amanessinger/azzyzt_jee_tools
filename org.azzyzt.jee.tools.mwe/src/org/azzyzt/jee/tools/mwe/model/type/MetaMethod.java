package org.azzyzt.jee.tools.mwe.model.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotatable;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;

public class MetaMethod implements Comparable<MetaMethod>, MetaAnnotatable {

	private MetaMethodId id;
    private String name;
    private MetaDeclaredType mdt;
    private MetaModifiers modifiers;
    private MetaType returnType;
    private List<MetaMethodParameter> signature = new ArrayList<MetaMethodParameter>();
	private Properties properties = new Properties(); // may be set by a synthesizing builder
	private List<MetaAnnotationInstance> metaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
    
    // TODO either introduce MetaEntityMethod or deny mapping annotations on methods

    public MetaMethod(MetaDeclaredType mdt, String methodName) {
    	this.mdt = mdt;
    	this.name = methodName;
    	this.id = new MetaMethodId(mdt, methodName);
    	mdt.addMethod(this);
    }

	public MetaMethodId getId() {
		return id;
	}

	public void setId(MetaMethodId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaDeclaredType getMdt() {
		return mdt;
	}

	public void setMdt(MetaDeclaredType mdt) {
		this.mdt = mdt;
	}

	public MetaModifiers getModifiers() {
		return modifiers;
	}

	public void setModifiers(MetaModifiers modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public int compareTo(MetaMethod other) {
		return this.getName().compareTo(other.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaMethod other = (MetaMethod) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public List<MetaMethodParameter> getSignature() {
		return signature;
	}

	public void addParameter(MetaMethodParameter p) {
		MetaType parameterMetaType = p.getType();
		mdt.addReferencedForeignType(parameterMetaType);
		this.signature.add(p);
	}

	public MetaType getReturnType() {
		return returnType;
	}

	public void setReturnType(MetaType returnType) {
		this.returnType = returnType;
	}
	
	public boolean isNotVoid() {
		return !returnType.getShortName().equals("void");
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setMetaAnnotationInstances(List<MetaAnnotationInstance> metaAnnotationInstances) {
		this.metaAnnotationInstances = metaAnnotationInstances;
	}

	@Override
	public void addMetaAnnotationInstance(MetaAnnotationInstance instance) {
		this.metaAnnotationInstances.add(instance);
	}
	
	@Override
	public List<MetaAnnotationInstance> getMetaAnnotationInstances() {
		return metaAnnotationInstances;
	}

	@Override
	public void addReferencedForeignType(MetaType referencedForeignType) {
		mdt.addReferencedForeignType(referencedForeignType);
	}

}
