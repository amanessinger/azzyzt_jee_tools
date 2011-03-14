package org.azzyzt.jee.tools.mwe.model.association;

import javax.persistence.ManyToOne;

import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class MetaManyToOne extends MetaAssociationEndpoint {
	
	private boolean optional;
	
	public MetaManyToOne(MetaField mf, ManyToOne ann) {
		super(mf);
		setFetch(ann.fetch());
		setCascades(ann.cascade());
		Class<?> targetEntity = ann.targetEntity();
		setTargetEntity(mf, targetEntity);
		optional = ann.optional();
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	
}
