package org.azzyzt.jee.tools.mwe.model.association;

import javax.persistence.OneToMany;

import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class MetaOneToMany extends MetaAssociationEndpoint {

	private boolean orphanRemoval;
	private String mappedBy;
	
	public MetaOneToMany(MetaField mf, OneToMany ann) {
		super(mf);
		setFetch(ann.fetch());
		setCascades(ann.cascade());
		Class<?> targetEntity = ann.targetEntity();
		setTargetEntity(mf, targetEntity);
		orphanRemoval = ann.orphanRemoval();
		mappedBy = ann.mappedBy();
	}

	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	public String getMappedBy() {
		return mappedBy;
	}
}
