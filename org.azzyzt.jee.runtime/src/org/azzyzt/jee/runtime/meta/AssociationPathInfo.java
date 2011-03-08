package org.azzyzt.jee.runtime.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.runtime.exception.InvalidFieldException;

public class AssociationPathInfo {
	
	private List<AssociationInfo> associationInfos = new ArrayList<AssociationInfo>();
	
	private Set<String> associationInfosSeen = new HashSet<String>();
	
	private String fieldSelector;

	public AssociationPathInfo(String fieldSelector) {
		super();
		this.fieldSelector = fieldSelector;
	}

	public String getFieldSelector() {
		return fieldSelector;
	}

	public List<AssociationInfo> getAssociationInfos() {
		return associationInfos;
	}

	public void addAssociationInfo(AssociationInfo ai) 
		throws InvalidFieldException 
	{
		if (associationInfosSeen.contains(ai.getId())) {
			throw new InvalidFieldException("Circular join, path fragment "+ai.getFieldSelector()+" repeated");
		}
		this.associationInfos.add(ai);
		associationInfosSeen.add(ai.getId());
	}

}
