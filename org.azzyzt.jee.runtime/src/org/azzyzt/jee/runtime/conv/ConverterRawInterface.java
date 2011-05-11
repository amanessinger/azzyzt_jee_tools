package org.azzyzt.jee.runtime.conv;

import org.azzyzt.jee.runtime.entity.EntityBase;
import org.azzyzt.jee.runtime.exception.DuplicateProxyIdException;
import org.azzyzt.jee.runtime.exception.EntityInstantiationException;
import org.azzyzt.jee.runtime.exception.EntityNotFoundException;
import org.azzyzt.jee.runtime.exception.InvalidIdException;
import org.azzyzt.jee.runtime.exception.InvalidProxyIdException;

public interface ConverterRawInterface {

	@SuppressWarnings("rawtypes")
	public EntityBase fromRawDto(Object in)
		throws EntityNotFoundException, EntityInstantiationException, InvalidIdException, 
			   DuplicateProxyIdException, InvalidProxyIdException;
	
	public Object fromEntityBase(@SuppressWarnings("rawtypes") EntityBase in);
	
}
