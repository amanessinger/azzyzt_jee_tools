/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.runtime.dto.query;

import org.azzyzt.jee.runtime.exception.QuerySyntaxException;


/**
 * Parent class for boolean expressions with one term. Well, basically <code>Not</code>.
 */
public abstract class UnaryBooleanExpression extends Expression implements Node {
	
	private static final long serialVersionUID = 1L;
	private Expression expression;
	
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Node#add(org.azzyzt.jee.runtime.dto.query.Expression)
	 */
	@Override
	public void add(Expression e) throws QuerySyntaxException {
		if (this.expression != null) {
			throw new QuerySyntaxException("A UnaryBooleanExpression can only have a single expression");
		}
		expression = e;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#isValid()
	 */
	@Override
	public boolean isValid() {
		return expression != null;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#getReplaceableBy()
	 */
	@Override
	public Expression getReplaceableBy() {
		return null;
	}

	@Override
	public String toString() {
		return "UnaryBooleanExpression [expression=" + expression + "]";
	}
}
