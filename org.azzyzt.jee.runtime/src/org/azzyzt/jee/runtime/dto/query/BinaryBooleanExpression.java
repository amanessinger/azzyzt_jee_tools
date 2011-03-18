package org.azzyzt.jee.runtime.dto.query;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public abstract class BinaryBooleanExpression extends Expression implements Node {
	
	private static final long serialVersionUID = 1L;
	private List<Expression> terms = new ArrayList<Expression>();
	private Expression replaceableBy = null;

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

	@Override
	public Expression getReplaceableBy() {
		return replaceableBy;
	}

	@Override
	public String toString() {
		return "BinaryBooleanExpression [type="+this.getClass().getSimpleName()+", terms=" + terms + "]";
	}

}
