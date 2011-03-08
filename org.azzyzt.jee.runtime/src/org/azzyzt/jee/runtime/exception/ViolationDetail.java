package org.azzyzt.jee.runtime.exception;

import java.io.Serializable;
import java.util.List;

public class ViolationDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String attributedItem;
    private String invalidItemValue;
    private String constraintName;
    private List<ConstraintProperty> constraintProperties;

    public ViolationDetail() { }

    public String getAttributedItem() {
        return attributedItem;
    }
    public void setAttributedItem(String attributedItem) {
        this.attributedItem = attributedItem;
    }
    public String getInvalidItemValue() {
        return invalidItemValue;
    }
    public void setInvalidItemValue(String invalidItemValue) {
        this.invalidItemValue = invalidItemValue;
    }
    public String getConstraintName() {
        return constraintName;
    }
    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }
    public void setConstraintProperties(List<ConstraintProperty> constraintProperties) {
        this.constraintProperties = constraintProperties;
    }
    public List<ConstraintProperty> getConstraintProperties() {
        return constraintProperties;
    }
}
