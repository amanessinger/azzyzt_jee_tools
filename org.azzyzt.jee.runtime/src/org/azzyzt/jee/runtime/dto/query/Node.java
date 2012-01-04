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
import org.azzyzt.jee.runtime.util.AttributedTags2QuerySpec;

/**
 * A <code>QuerySpec</code> has an <code>Expression</code>, but is itself
 * used by the parser as root of the expression tree. In order to do that, 
 * both implement <code>Node</code>.
 * 
 * @see QuerySpec
 * @see QuerySyntaxExpression
 * @see AttributedTags2QuerySpec
 */
public interface Node {
	
	/**
	 * Adds an expression to the current <code>Node</code>. Invalid XML can
	 * cause a <code>QuerySyntaxExpression</code>.
	 * @param expression the expression to be added
	 * @throws QuerySyntaxException
	 */
	public void add(Expression expression) throws QuerySyntaxException;
	
	/**
	 * @return <code>true</code> if the expression is valid
	 */
	public boolean isValid();
	
	/**
	 * @return a simpler and equivalent expression or <code>null</code>.
	 */
	public Expression getReplaceableBy();

}
