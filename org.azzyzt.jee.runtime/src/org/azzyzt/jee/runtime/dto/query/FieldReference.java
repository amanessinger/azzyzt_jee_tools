package org.azzyzt.jee.runtime.dto.query;

public class FieldReference extends Operand implements FieldReferer {
	
	private String fieldName;
	
	private boolean isCaseSensitive;

	public FieldReference() { }

	public FieldReference(String fieldName) {
		super();
		this.fieldName = fieldName;
		setCaseSensitive(false);
	}

	public FieldReference(String fieldName, boolean isCaseSensitive) {
		super();
		this.fieldName = fieldName;
		setCaseSensitive(isCaseSensitive);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}

	@Override
	public boolean isValid() {
		return fieldName != null && ! fieldName.isEmpty();
	}
}