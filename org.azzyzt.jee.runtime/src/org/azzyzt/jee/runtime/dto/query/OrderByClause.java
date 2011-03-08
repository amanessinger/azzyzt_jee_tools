package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderby")
public class OrderByClause implements FieldReferer, Serializable {
	
	private static final long serialVersionUID = 1L;

	private String fieldName;
	private boolean isAscending;
	
	public OrderByClause() { }
	
	public OrderByClause(String fieldName, boolean isAscending) {
		super();
		this.fieldName = fieldName;
		this.isAscending = isAscending;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isAscending() {
		return isAscending;
	}

	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public String toString() {
		return "OrderByClause [fieldName=" + fieldName + ", isAscending=" + isAscending + "]";
	}
	
}
