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

public class IdTranslator {
	
	private Map<Number, Object> table = new HashMap<Number, Object>();
	
	public void addTranslation(Number proxy, Object value) throws DuplicateProxyIdException {
		if (table.containsKey(proxy)) {
			throw new DuplicateProxyIdException(proxy.toString());
		}
		table.put(proxy, value);
	}

	public Object translate(Number proxy) throws InvalidProxyIdException {
		if (proxy == null || proxy.longValue() >= 0) {
			return proxy;
		}
		if (!table.containsKey(proxy)) {
			throw new InvalidProxyIdException(proxy.toString());
		}
		return table.get(proxy);
	}

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
