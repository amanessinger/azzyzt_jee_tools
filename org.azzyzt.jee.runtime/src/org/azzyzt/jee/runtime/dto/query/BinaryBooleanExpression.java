/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Abstract boolean expression class with some common code for validity checking 
 * and simplification 
 */
public abstract class BinaryBooleanExpression extends Expression implements Node {
	
	private static final long serialVersionUID = 1L;
	private List<Expression> terms = new ArrayList<Expression>();
	private Expression replaceableBy = null;

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean result = false;
		if (terms == null || terms.size() == 0) {
			result = false;
		} else if (terms.size() == 1) {
			// Expression can be replaced by single term
			replaceableBy = terms.get(0);
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Node#add(org.azzyzt.jee.runtime.dto.query.Expression)
	 */
	@Override
	public void add(Expression e) {
		terms.add(e);
	}

	@XmlElement(nillable=false, required=true)
	public List<Expression> getTerms() {
		return terms;
	}

	public void setTerms(List<Expression> terms) {
		this.terms = terms;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Expression#getReplaceableBy()
	 */
	@Override
	public Expression getReplaceableBy() {
		return replaceableBy;
	}

	@Override
	public String toString() {
		return "BinaryBooleanExpression [type="+this.getClass().getSimpleName()+", terms=" + terms + "]";
	}

}
