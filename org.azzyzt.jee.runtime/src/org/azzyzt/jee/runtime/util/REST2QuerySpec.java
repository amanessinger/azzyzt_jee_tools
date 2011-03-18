package org.azzyzt.jee.runtime.util;

import org.azzyzt.jee.runtime.dto.query.QuerySpec;
import org.azzyzt.jee.runtime.exception.QuerySyntaxException;

public interface REST2QuerySpec {

	public abstract QuerySpec fromXML(String xml) throws QuerySyntaxException;

}