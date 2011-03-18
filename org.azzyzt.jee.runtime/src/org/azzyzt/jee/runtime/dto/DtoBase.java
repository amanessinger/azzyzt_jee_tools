package org.azzyzt.jee.runtime.dto;

import org.azzyzt.jee.runtime.entity.EntityBase;

public abstract class DtoBase<ID> {

    public boolean hasId() {
        return EntityBase.couldBeIdValue(getId());
    }
 
	public abstract ID getId();

	public abstract void setId(ID id);
}
