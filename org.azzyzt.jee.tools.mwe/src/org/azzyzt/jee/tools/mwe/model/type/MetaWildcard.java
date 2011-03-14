package org.azzyzt.jee.tools.mwe.model.type;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaWildcard extends MetaType {

	private List<MetaType> upperBounds = new ArrayList<MetaType>();
	private List<MetaType> lowerBounds = new ArrayList<MetaType>();

	public static MetaWildcard forName(String name) {
		MetaWildcard result = (MetaWildcard)MetaTypeRegistry.metaTypeForName(name);
		if (result == null || !(result instanceof MetaWildcard)) {
			result = new MetaWildcard(name);
		}
		return result;
	}
	
	protected MetaWildcard(String name) {
		super(name);
	}

	public List<MetaType> getUpperBounds() {
		return upperBounds;
	}

	public void addUpperBound(MetaType mdt) {
		this.upperBounds.add(mdt);
	}

	public List<MetaType> getLowerBounds() {
		return lowerBounds;
	}

	public void addLowerBound(MetaType mdt) {
		this.lowerBounds.add(mdt);
	}

	@Override
	public String getShortName() {
		if (upperBounds.isEmpty() && lowerBounds.isEmpty()) {
			return getName();
		} else if (lowerBounds.isEmpty() && !upperBounds.isEmpty() && upperBounds.get(0).getName().equals(Object.class.getName())) {
			return getName();
		} else if (lowerBounds.isEmpty() && !upperBounds.isEmpty()) {
			return "? extends "+upperBounds.get(0).getShortName();
		} else if (upperBounds.isEmpty() && !lowerBounds.isEmpty()) {
			return "? super "+lowerBounds.get(0).getShortName();
		} else {
			throw new ToolError("A bounded wildcard can either have a lower or an upper bound, not both");
		}
	}

}
