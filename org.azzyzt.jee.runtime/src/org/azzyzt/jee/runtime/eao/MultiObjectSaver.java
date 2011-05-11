package org.azzyzt.jee.runtime.eao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.azzyzt.jee.runtime.conv.ConverterRawInterface;
import org.azzyzt.jee.runtime.entity.EntityBase;
import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.exception.DuplicateProxyIdException;
import org.azzyzt.jee.runtime.exception.EntityInstantiationException;
import org.azzyzt.jee.runtime.exception.EntityNotFoundException;
import org.azzyzt.jee.runtime.exception.InvalidArgumentException;
import org.azzyzt.jee.runtime.exception.InvalidIdException;
import org.azzyzt.jee.runtime.exception.InvalidProxyIdException;
import org.azzyzt.jee.runtime.meta.InvocationRegistryInterface;
import org.azzyzt.jee.runtime.meta.TypeMetaInfoInterface;

public class MultiObjectSaver {
	
	
	private Map<Class<?>, ConverterRawInterface> converterForDto = new HashMap<Class<?>, ConverterRawInterface>();
	private IdTranslator idTranslator = new IdTranslator();
	
	public Object[] store(
			EaoBase eao, 
			InvocationRegistryInterface invocationRegistry, 
			TypeMetaInfoInterface tmi, 
			@SuppressWarnings("rawtypes") List dtos
			)
	throws EntityNotFoundException, AccessDeniedException, InvalidArgumentException, 
		   InvalidIdException, DuplicateProxyIdException, InvalidProxyIdException, 
		   EntityInstantiationException
	{
		Object[] result = new Object[dtos.size()];
		
		for (int i = 0; i < dtos.size(); i++) {
			Object dto = dtos.get(i);
			ConverterRawInterface conv = converterFromDto(eao, invocationRegistry, tmi, dto);
			@SuppressWarnings("rawtypes")
			EntityBase e = conv.fromRawDto(dto);
			result[i] = conv.fromEntityBase(e);
		}
		
		return result;
	}

	private ConverterRawInterface converterFromDto(
			EaoBase eao,
			InvocationRegistryInterface invocationRegistry,
			TypeMetaInfoInterface tmi, 
			Object dto
			)
	throws InvalidArgumentException, AccessDeniedException 
	{
		ConverterRawInterface conv;
		Class<?> dtoClass = dto.getClass();
		if (converterForDto.containsKey(dtoClass)) {
			conv = converterForDto.get(dtoClass);
		} else {
			Class<?> converterClass = tmi.getConverterForDto(dtoClass);
			try {
				Constructor<?> constructor = converterClass.getConstructor(
						EaoBase.class, 
						InvocationRegistryInterface.class, 
						TypeMetaInfoInterface.class,
						IdTranslator.class
						);
				conv = (ConverterRawInterface)constructor.newInstance(eao, invocationRegistry, tmi, idTranslator);
				converterForDto.put(dtoClass, conv);
			} catch (SecurityException e) {
				throw new AccessDeniedException();
			} catch (NoSuchMethodException e) {
				throw new InvalidArgumentException(dto.toString());
			} catch (IllegalArgumentException e) {
				throw new InvalidArgumentException(dto.toString());
			} catch (InstantiationException e) {
				throw new InvalidArgumentException(dto.toString());
			} catch (IllegalAccessException e) {
				throw new AccessDeniedException();
			} catch (InvocationTargetException e) {
				throw new InvalidArgumentException(dto.toString());
			}
		}
		return conv;
	}

}
