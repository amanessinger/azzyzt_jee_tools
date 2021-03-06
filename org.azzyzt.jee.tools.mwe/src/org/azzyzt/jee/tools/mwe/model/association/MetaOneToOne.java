/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

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
