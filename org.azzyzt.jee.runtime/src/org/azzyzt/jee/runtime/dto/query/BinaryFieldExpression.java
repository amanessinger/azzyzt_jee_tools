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
 * A binary field expression is a <code>FieldExpresssion</code> with a binary operator.
 * It always involves one field, the second <code>Operand</code> can either be a <code>Literal</code>
 * or a <code>FieldReference</code> as well.
 * 
 * @see Operand
 * @see BinaryFieldOperator
 * @see FieldReference
 */
public class BinaryFieldExpression extends FieldExpression {

	private static final long serialVersionUID = 1L;

	private BinaryFieldOperator op;
	
	private Operand operand;
		
	public BinaryFieldExpression() { }
	
	public BinaryFieldExpression(FieldReference fieldReference, BinaryFieldOperator op, Operand operand, boolean isNegated) {
		super(fieldReference, isNegated);
		this.op = op;
		this.operand = operand;
	}

	public BinaryFieldOperator getOp() {
		return op;
	}

	public void setOp(BinaryFieldOperator op) {
		this.op = op;
	}

	public Operand getOperand() {
		return operand;
	}

	public void setOperand(Operand operand) {
		this.operand = operand;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.FieldExpression#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean result = super.isValid() && op != null && operand.isValid();
		return result;
	}
	
}
