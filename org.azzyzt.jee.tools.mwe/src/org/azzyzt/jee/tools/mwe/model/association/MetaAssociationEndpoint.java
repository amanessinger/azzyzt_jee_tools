/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
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

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import org.azzyzt.jee.runtime.meta.JoinType;
import org.azzyzt.jee.runtime.meta.RequiredSelectionType;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaAssemblage;
import org.azzyzt.jee.tools.mwe.model.type.MetaCollection;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public abstract class MetaAssociationEndpoint {

	private MetaField sourceField;
	
	private MetaDeclaredType targetEntity;
	private FetchType fetch = FetchType.LAZY;
	private CascadeType[] cascades;

	public MetaAssociationEndpoint(MetaField sourceField) {
		super();
		this.sourceField = sourceField;
	}

	public void setSourceField(MetaField sourceField) {
		this.sourceField = sourceField;
	}

	public MetaField getSourceField() {
		return sourceField;
	}

	public MetaDeclaredType getTargetEntity() {
		return targetEntity;
	}

	private void setTargetEntity(MetaDeclaredType targetEntity) {
		this.targetEntity = targetEntity;
	}

	protected void setTargetEntity(MetaField mf, Class<?> targetEntity) {
		if (targetEntity.equals(void.class)) {
			/*
			 * JPA gives us a default of void.class if the attribute is not explicitly specified. 
			 * We prefer the actual type instead
			 */
			MetaType fieldMetaType = mf.getFieldType();
			if (fieldMetaType instanceof MetaCollection) {
				if (((MetaCollection)fieldMetaType).getMemberTypeCount() == 2) {
					fieldMetaType = ((MetaAssemblage)fieldMetaType).getMemberType(1);
				} else {
					fieldMetaType = ((MetaAssemblage)fieldMetaType).getMemberType(0);
				}
			}
			assert fieldMetaType instanceof MetaEntity;
			setTargetEntity((MetaDeclaredType)fieldMetaType);
			/*
			 * Now fix the source field
			 */
			for (MetaAnnotationInstance mai : mf.getMetaAnnotationInstances()) {
				if (mai.getFqName().matches("^javax.persistence.(One|Many)To(One|Many)$")) {
					if (mai.hasElement("targetEntity")) {
						mai.dropElement("targetEntity");
					}
					break;
				}
			}
		} else {
			MetaDeclaredType metaTargetEntity = MetaEntity.forType(
					targetEntity
			);
			setTargetEntity(metaTargetEntity);
		}
	}

	public FetchType getFetch() {
		return fetch;
	}
	
	public boolean isFetchTypeEager() {
		return fetch.equals(FetchType.EAGER);
	}

	public void setFetch(FetchType fetch) {
		if (fetch != null) {
			this.fetch = fetch;
		}
	}

	public CascadeType[] getCascades() {
		return cascades;
	}

	public void setCascades(CascadeType[] cascades) {
		this.cascades = cascades;
	}
	
	public String getRequiredSelectionType() {
		MetaType fieldMetaType = sourceField.getFieldType();
		if (fieldMetaType instanceof MetaCollection) {
			return RequiredSelectionType.Distinct.toString();
		} else if (fieldMetaType instanceof MetaEntity) {
			return RequiredSelectionType.Normal.toString();
		} else {
			throw new ToolError("Association field type can only be a MetaCollection or a MetaEntity");
		}
	}

	public String getJoinType() {
		MetaType fieldMetaType = sourceField.getFieldType();
		if (fieldMetaType instanceof MetaCollection) {
			MetaCollection mc = (MetaCollection)fieldMetaType;
			if (mc.isList()) {
				return JoinType.List.toString();
			} else if (mc.isSet()) {
				return JoinType.Set.toString();
			} else if (mc.isMap()) {
				return JoinType.Map.toString();
			} else {
				throw new ToolError("Meta collection unsupported as join type");
			}
		} else if (fieldMetaType instanceof MetaEntity) {
			return JoinType.Simple.toString();
		} else {
			throw new ToolError("Association field type can only be a MetaCollection or a MetaEntity");
		}
	}

}
