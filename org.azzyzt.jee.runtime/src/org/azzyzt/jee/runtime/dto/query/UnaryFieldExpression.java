package org.azzyzt.jee.runtime.dto.query;

public class UnaryFieldExpression extends FieldExpression {
	
	public UnaryFieldExpression() { }

	public UnaryFieldExpression(FieldReference fieldReference, UnaryFieldOperator op, boolean isNegated) {
		super(fieldReference, isNegated);
		this.op = op;
	}

	private static final long serialVersionUID = 1L;

	private UnaryFieldOperator op;
	
	@Override
	public boolean isValid() {
		boolean result 
			= super.isValid();
		return result;
	}

	public UnaryFieldOperator getOp() {
		return op;
	}

	public void setOp(UnaryFieldOperator op) {
		this.op = op;
	}

}
