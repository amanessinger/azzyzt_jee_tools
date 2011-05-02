package org.azzyzt.jee.runtime.eao;

import java.util.HashMap;
import java.util.Map;

import org.azzyzt.jee.runtime.exception.InvalidIdException;

public class IdTranslator {
	
	private Map<Number, Number> table = new HashMap<Number, Number>();
	
	public void addTranslation(Number proxy, Number value) {
		table.put(proxy, value);
	}

	public Number translate(Number proxy) throws InvalidIdException {
		if (!table.containsKey(proxy)) {
			throw new InvalidIdException(proxy.toString());
		}
		return table.get(proxy);
	}
}
