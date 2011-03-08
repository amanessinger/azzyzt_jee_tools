package org.azzyzt.jee.runtime.dto.query;

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

	@Override
	public boolean isValid() {
		boolean result = super.isValid() && op != null && operand.isValid();
		return result;
	}
	
}
