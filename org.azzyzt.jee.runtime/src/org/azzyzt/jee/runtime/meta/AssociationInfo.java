package org.azzyzt.jee.runtime.meta;


public class AssociationInfo {

	private String fieldSelector;
	private RequiredSelectionType requiredSelectionType;
	private Class<?> joinFrom;
	private Class<?> joinTo;
	private JoinType joinType;

	public AssociationInfo(
			String fieldSelector, 
			RequiredSelectionType requiredSelectionType, 
			Class<?> joinFrom, 
			Class<?> joinTo, 
			JoinType joinType
	) {
		super();
		this.fieldSelector = fieldSelector;
		this.requiredSelectionType = requiredSelectionType;
		this.joinFrom = joinFrom;
		this.joinTo = joinTo;
		this.joinType = joinType;
	}

	public String getFieldSelector() {
		return fieldSelector;
	}

	public RequiredSelectionType isForcingDistinct() {
		return requiredSelectionType;
	}

	public Class<?> getJoinFrom() {
		return joinFrom;
	}

	public Class<?> getJoinTo() {
		return joinTo;
	}

	public JoinType getJoinType() {
		return joinType;
	}
	
	public String getId() {
		return joinFrom.getCanonicalName()+"->"+fieldSelector;
	}

}
