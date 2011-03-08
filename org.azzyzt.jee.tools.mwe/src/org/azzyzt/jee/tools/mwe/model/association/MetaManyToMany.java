package org.azzyzt.jee.tools.mwe.model.association;

import javax.persistence.ManyToMany;

import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class MetaManyToMany extends MetaAssociationEndpoint {
	
	private String mappedBy;
	
	public MetaManyToMany(MetaField mf, ManyToMany ann) {
		super(mf);
		setFetch(ann.fetch());
		setCascades(ann.cascade());
		Class<?> targetEntity = ann.targetEntity();
		setTargetEntity(mf, targetEntity);
		mappedBy = ann.mappedBy();
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	
}
