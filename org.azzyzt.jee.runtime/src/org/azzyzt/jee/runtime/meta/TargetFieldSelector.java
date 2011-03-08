package org.azzyzt.jee.runtime.meta;

/**
 * Used as parameter value
 */
public class TargetFieldSelector {

	private Class<?> targetEntity;
	private String fieldSelector;

	public TargetFieldSelector(Class<?> targetEntity, String fieldSelector) {
		super();
		this.targetEntity = targetEntity;
		this.fieldSelector = fieldSelector;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public String getFieldSelector() {
		return fieldSelector;
	}

}
