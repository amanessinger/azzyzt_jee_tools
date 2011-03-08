package org.azzyzt.jee.tools.mwe.model.type;

public class MetaEnumConstant implements MetaValue {

	private String shortName;
	private String shortQualifiedName;
	private int ordinal;

	public MetaEnumConstant(String shortName, String shortQualifiedName, int ordinal) {
		super();
		this.shortName = shortName;
		this.shortQualifiedName = shortQualifiedName;
		this.ordinal = ordinal;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortQualifiedName() {
		return shortQualifiedName;
	}

	public void setShortQualifiedName(String shortQualifiedName) {
		this.shortQualifiedName = shortQualifiedName;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	@Override
	public Object getValue() {
		return ordinal;
	}

	@Override
	public String toString() {
		return shortQualifiedName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((shortQualifiedName == null) ? 0 : shortQualifiedName
						.hashCode());
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
		MetaEnumConstant other = (MetaEnumConstant) obj;
		if (shortQualifiedName == null) {
			if (other.shortQualifiedName != null)
				return false;
		} else if (!shortQualifiedName.equals(other.shortQualifiedName))
			return false;
		return true;
	}

}
