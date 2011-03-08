package org.azzyzt.jee.tools.mwe.model.association;

import javax.persistence.OneToOne;

import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class MetaOneToOne extends MetaAssociationEndpoint {
	
	private boolean orphanRemoval;
	private boolean optional;
	private String mappedBy;
	
	public MetaOneToOne(MetaField mf, OneToOne ann) {
		super(mf);
		setFetch(ann.fetch());
		setCascades(ann.cascade());
		Class<?> targetEntity = ann.targetEntity();
		setTargetEntity(mf, targetEntity);
		orphanRemoval = ann.orphanRemoval();
		optional = ann.optional();
		mappedBy = ann.mappedBy();
	}

	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	public void setOrphanRemoval(boolean orphanRemoval) {
		this.orphanRemoval = orphanRemoval;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	
}
