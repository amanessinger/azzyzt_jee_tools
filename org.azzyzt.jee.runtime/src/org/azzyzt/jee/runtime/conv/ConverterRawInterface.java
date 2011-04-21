package org.azzyzt.jee.runtime.conv;

import org.azzyzt.jee.runtime.entity.EntityBase;
import org.azzyzt.jee.runtime.exception.EntityInstantiationException;
import org.azzyzt.jee.runtime.exception.EntityNotFoundException;
import org.azzyzt.jee.runtime.exception.InvalidIdException;

public interface ConverterRawInterface {

	@SuppressWarnings("rawtypes")
	public EntityBase fromRawDto(Object in)
		throws EntityNotFoundException, EntityInstantiationException, InvalidIdException;
	
	public Object fromEntityBase(@SuppressWarnings("rawtypes") EntityBase in);
	
}
