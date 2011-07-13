package org.azzyzt.jee.runtime.eao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;

import org.azzyzt.jee.runtime.exception.DuplicateProxyIdException;
import org.azzyzt.jee.runtime.exception.InvalidIdException;
import org.azzyzt.jee.runtime.exception.InvalidProxyIdException;

/**
 * <p>When a graph of new but connected objects is persisted, the DTOs can't be connected
 * by each other's IDs, because at that time no object has an ID yet. Instead of
 * real IDs we use temporary negative proxy ID values and translate them on the fly.</p>
 * 
 * <p>ID translation happens inside of the generated converters. <code>MultiObjectSaver</code>
 * creates one instance of <code>IdTranslator</code> and then traverses the list of DTOs. 
 * For each DTO it creates or uses an already existing converter, passing the common
 * translator into the converters. The DTOs are expected to be in an order such that 
 * no proxy ID is referenced, that is not already defined.</p>
 * 
 * @see MultiObjectSaver
 */
public class IdTranslator {
	
	private Map<Number, Object> table = new HashMap<Number, Object>();
	
	/**
	 * @param proxy a negative proxy ID value
	 * @param value a positive real ID value
	 * @throws DuplicateProxyIdException
	 */
	public void addTranslation(Number proxy, Object value) throws DuplicateProxyIdException {
		if (table.containsKey(proxy)) {
			throw new DuplicateProxyIdException(proxy.toString());
		}
		table.put(proxy, value);
	}

	/**
	 * Translates a proxy ID to a real ID. If the proxy is <code>null</code> or positive,
	 * the proxy is returned. If no translation for a negative proxy value is found, an
	 * exception is thrown. This could be due to a wrong order of the DTO list.
	 * 
	 * @param proxy a negative proxy ID value or <code>null</code>
	 * @return the translation or <code>null</code>
	 * @throws InvalidProxyIdException
	 */
	public Object translate(Number proxy) throws InvalidProxyIdException {
		if (proxy == null || proxy.longValue() >= 0) {
			return proxy;
		}
		if (!table.containsKey(proxy)) {
			throw new InvalidProxyIdException(proxy.toString());
		}
		return table.get(proxy);
	}

	/**
	 * Translates the IDs in an embedded ID.
	 * 
	 * @param embeddedId an embedded ID
	 * @return the same ID with ID fields translated
	 * @throws InvalidProxyIdException
	 * @throws InvalidIdException
	 */
	public Object translate(Serializable embeddedId) throws InvalidProxyIdException, InvalidIdException {
		Class<? extends Serializable> idClazz = embeddedId.getClass();
		Embeddable embeddable = idClazz.getAnnotation(Embeddable.class);
		if (embeddable == null) {
			throw new InvalidIdException(idClazz.getSimpleName()+" is not @Embeddable");
		}
		Field[] declaredFields = idClazz.getDeclaredFields();
		for (Field f : declaredFields) {
			int modifiers = f.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				continue;
			}
			if (!Number.class.isAssignableFrom(f.getType())) {
				continue;
			}
			/*
			 *  We assume all non-static fields of embedded IDs that extend Numeric
			 *  to be IDs that potentially are in need of translation
			 */
			Number proxy = null;
			try {
				boolean wasAccessible = f.isAccessible();
				f.setAccessible(true);
				proxy = (Number)f.get(embeddedId);
				if (proxy == null || proxy.longValue() >= 0) {
					continue;
				}
				if (!table.containsKey(proxy)) {
					throw new InvalidProxyIdException(proxy.toString());
				}
				f.set(embeddedId, table.get(proxy));
				f.setAccessible(wasAccessible);
			} catch (IllegalArgumentException e) {
				throw new InvalidProxyIdException(idClazz.getSimpleName()+"."+f.getName()+" is inaccessible");
			} catch (IllegalAccessException e) {
				throw new InvalidProxyIdException(idClazz.getSimpleName()+"."+f.getName()+" is inaccessible");
			}
		}
		return embeddedId;
	}
}
