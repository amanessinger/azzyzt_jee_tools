package org.azzyzt.jee.runtime.dto.query;

import org.azzyzt.jee.runtime.exception.QuerySyntaxException;

public interface Node {
	
	public void add(Expression expression) throws QuerySyntaxException;
	
	public boolean isValid();
	
	public Expression getReplaceableBy();

}
