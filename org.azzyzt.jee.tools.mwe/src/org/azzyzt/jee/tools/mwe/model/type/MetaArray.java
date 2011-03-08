package org.azzyzt.jee.tools.mwe.model.type;

public class MetaArray extends MetaAssemblage {

	public static MetaArray forName(String name) {
		MetaArray result = (MetaArray)MetaTypeRegistry.metaTypeForName(name);
		if (result == null || !(result instanceof MetaArray)) {
			result = new MetaArray(name);
		}
		return result;
	}
	
	protected MetaArray(String name) {
		super(name);
	}

	@Override
	public boolean isAcceptableMemberType(MetaType mt) {
		// arrays can contain everything
		return true;
	}

	@Override
	public int maximumNumberOfMemberTypes() {
		// Array can't have more than one member type, can they?
		return 1;
	}

	@Override
	public String getShortName() {
		return getMemberType(0).getShortName()+"[]";
	}
}
