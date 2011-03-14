package org.azzyzt.jee.tools.mwe.model.type;

public class MetaMethodId {

	private String id;
	
	public MetaMethodId(MetaType mdt, String methodName) {
		this.id = String.format("%s.%s()", mdt.getId(), methodName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MetaMethodId other = (MetaMethodId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id;
	}
}
