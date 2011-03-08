package org.azzyzt.jee.runtime.entity;


public abstract class EntityBase<ID> {

	public static boolean couldBeIdValue(Object id) {
		
		if (id == null) return false;
		
		if (id instanceof Number) {
			long longId = ((Number)id).longValue();
			return longId > 0L; // we may need to relax this to "longId != 0L" for some databases
		} else {
			return true;
		}
	}

	public boolean likelyHasId() {
		return couldBeIdValue(getId());
	}

	public abstract ID getId();

	public abstract void setId(ID id);
}
