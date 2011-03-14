package org.azzyzt.jee.tools.mwe.model.type;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public abstract class MetaAssemblage extends MetaType {
	
	private List<MetaType> memberTypes = new ArrayList<MetaType>();
	
	protected MetaAssemblage(String name) {
		super(name);
	}
	
	public abstract int maximumNumberOfMemberTypes();
	public abstract boolean isAcceptableMemberType(MetaType mt);

	public MetaType getMemberType(int index) {
		return memberTypes.get(index);
	}

	public void addMemberType(MetaType mt) {
		if (memberTypes.contains(mt)) return;
		
		if (memberTypes.size() == maximumNumberOfMemberTypes()) {
			throw new ToolError("Maximum number of member types for "+this+" reached, can't add any more");
		}
		if (isAcceptableMemberType(mt)) {
			memberTypes.add(mt);
		} else {
			throw new ToolError(mt+" is not an acceptable member type for "+this);
		}
	}

	public List<MetaType> getMemberTypes() {
		return memberTypes;
	}
	
	public int getMemberTypeCount() {
		return memberTypes.size();
	}
}
