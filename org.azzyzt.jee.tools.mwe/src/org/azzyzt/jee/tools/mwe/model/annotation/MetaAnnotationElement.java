package org.azzyzt.jee.tools.mwe.model.annotation;

import java.lang.reflect.Method;

import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class MetaAnnotationElement {

	private String name;
	private MetaType metaType;
	private Method method;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaType getMetaType() {
		return metaType;
	}

	public void setMetaType(MetaType metaType) {
		this.metaType = metaType;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
