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

/**
 * A ternary field expression is a <code>FieldExpresssion</code> with a ternary operator.
 * It always involves one field, the other two <code>Operands</code> can each either be a <code>Literal</code>
 * or a <code>FieldReference</code> as well.
 * 
 * @see Operand
 * @see TernaryFieldOperator
 * @see FieldReference
 */
public class TernaryFieldExpression extends FieldExpression {

	private static final long serialVersionUID = 1L;

	private TernaryFieldOperator op;
	
	private Operand operand2;
	
	private Operand operand3;
	
	public TernaryFieldExpression() { }
	
	public TernaryFieldExpression(FieldReference fieldReference, TernaryFieldOperator op, 
			Operand operand2, Operand operand3, boolean isNegated) {
		super(fieldReference, isNegated);
		this.op = op;
		this.operand2 = operand2;
		this.operand3 = operand3;
	}

	public TernaryFieldOperator getOp() {
		return op;
	}

	public void setOp(TernaryFieldOperator op) {
		this.op = op;
	}

	public Operand getOperand2() {
		return operand2;
	}

	public void setOperand2(Operand operand2) {
		this.operand2 = operand2;
	}

	public Operand getOperand3() {
		return operand3;
	}
	
	public void setOperand3(Operand operand3) {
		this.operand3 = operand3;
	}
	
	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldExpression#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean result = super.isValid() && op != null && operand2.isValid() && operand3.isValid();
		return result;
	}

}
