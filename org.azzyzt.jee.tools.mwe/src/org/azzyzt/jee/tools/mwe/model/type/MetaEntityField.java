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

package org.azzyzt.jee.tools.mwe.model.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.azzyzt.jee.runtime.annotation.CreateTimestamp;
import org.azzyzt.jee.runtime.annotation.CreateUser;
import org.azzyzt.jee.runtime.annotation.Internal;
import org.azzyzt.jee.runtime.annotation.ModificationTimestamp;
import org.azzyzt.jee.runtime.annotation.ModificationUser;
import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;
import org.azzyzt.jee.tools.mwe.model.association.MetaManyToMany;
import org.azzyzt.jee.tools.mwe.model.association.MetaManyToOne;
import org.azzyzt.jee.tools.mwe.model.association.MetaOneToMany;
import org.azzyzt.jee.tools.mwe.model.association.MetaOneToOne;

public class MetaEntityField extends MetaField {

	private boolean isIdField = false;
	private boolean isInternal = false;
	private boolean isCreateUserField = false;
	private boolean isModificationUserField = false;
	private boolean isCreateTimestampField = false;
	private boolean isModificationTimestampField = false;
	private boolean isHoldingMultivaluedAssociationEndpoint = false;
	private boolean isNotAutomaticallySet = true;
	private MetaAssociationEndpoint associationEndpoint = null;

	private String timestampFormat;
	
	public MetaEntityField(MetaEntity me, String fieldName) {
		super(me, fieldName);
	}

	public MetaAssociationEndpoint getAssociationEndpoint() {
		return associationEndpoint;
	}

	public void setAssociationEndpoint(MetaAssociationEndpoint associationEndpoint) {
		this.associationEndpoint = associationEndpoint;
	}

	public boolean isIdField() {
		return isIdField;
	}

	public void setIdField(boolean isIdField) {
		this.isIdField = isIdField;
	}

	public boolean isInternal() {
		return isInternal;
	}

	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}

	public boolean isHoldingAssociationEndpoint() {
		return associationEndpoint != null;
	}

	public boolean isHoldingMultivaluedAssociationEndpoint() {
		return isHoldingMultivaluedAssociationEndpoint;
	}

	public void setHoldingMultivaluedAssociationEndpoint(
			boolean isHoldingMultivaluedAssociationEndpoint) {
		this.isHoldingMultivaluedAssociationEndpoint = isHoldingMultivaluedAssociationEndpoint;
	}
	
	@Override
	public void processAnnotations(Field fld) {
		super.processAnnotations(fld);
		for (Annotation fa : fld.getAnnotations()) {
			// We could use the already constructed MetaAnnotationInstances, but this is easier
			MetaEntity metaEntity = (MetaEntity)getOwnerMdt();
			if (fa.annotationType().equals(Internal.class)) {
				setInternal(true);
			} else if (fa.annotationType().equals(CreateUser.class)) {
				// we need this to be a String
				if (! isStringField() ) {
					String msg = "Field attributed with @CreateUser must be a string";
					throw new ToolError(msg);
				}
				metaEntity.setCreateUserField(this);
				isCreateUserField = true;
				isNotAutomaticallySet = false;
			} else if (fa.annotationType().equals(ModificationUser.class)) {
				// we need this to be a String
				if (! isStringField() ) {
					String msg = "Field attributed with @ModificationUser must be a string";
					throw new ToolError(msg);
				}
				metaEntity.setModificationUserField(this);
				isModificationUserField = true;
				isNotAutomaticallySet = false;
			} else if (fa.annotationType().equals(CreateTimestamp.class)) {
				// we need this to be a String, Calendar or Date
				if (isStringField()) {
					// needs a format attribute
					CreateTimestamp tst = (CreateTimestamp)fa;
					timestampFormat = tst.format();
					if (timestampFormat == null || timestampFormat.isEmpty()) {
						String msg = "Field attributed with @CreateTimestamp needs timestamp format";
						throw new ToolError(msg);
					}
				} else if (isDateField() || isCalendarField()) {
					// nothing to do
				} else {
					String msg = "Field attributed with @CreateTimestamp must be a String, Date or Calendar";
					throw new ToolError(msg);
				}
				metaEntity.setCreateTimestampField(this);
				isCreateTimestampField = true;
				isNotAutomaticallySet = false;
			} else if (fa.annotationType().equals(ModificationTimestamp.class)) {
				// we need this to be a String, Calendar or Date
				if (isStringField()) {
					// needs a format attribute
					ModificationTimestamp tst = (ModificationTimestamp)fa;
					timestampFormat = tst.format();
					if (timestampFormat == null || timestampFormat.isEmpty()) {
						String msg = "Field attributed with @ModificationTimestamp needs timestamp format";
						throw new ToolError(msg);
					}
				} else if (isDateField() || isCalendarField()) {
					// nothing to do
				} else {
					String msg = "Field attributed with @ModificationTimestamp must be a String, Date or Calendar";
					throw new ToolError(msg);
				}
				metaEntity.setModificationTimestampField(this);
				isModificationTimestampField = true;
				isNotAutomaticallySet = false;
			} else if (fa.annotationType().equals(Id.class)) {
				metaEntity.setIdField(this);
				setIdField(true);
			} else if (fa.annotationType().equals(EmbeddedId.class)) {
				metaEntity.setIdField(this);
				metaEntity.setCombinedId(true);
				metaEntity.setCombinedIdType(getFieldType());
				setIdField(true);
		    } else if (fa.annotationType().equals(OneToOne.class)) {
		    	OneToOne a = (OneToOne)fa;
				associationEndpoint = new MetaOneToOne(this, a);
		    } else if (fa.annotationType().equals(ManyToOne.class)) {
		    	ManyToOne a = (ManyToOne)fa;
		    	associationEndpoint = new MetaManyToOne(this, a);
		    } else if (fa.annotationType().equals(OneToMany.class)) {
		    	OneToMany a = (OneToMany)fa;
		    	associationEndpoint = new MetaOneToMany(this, a);
		    	setHoldingMultivaluedAssociationEndpoint(true);
		    } else if (fa.annotationType().equals(ManyToMany.class)) {
		    	ManyToMany a = (ManyToMany)fa;
		    	associationEndpoint = new MetaManyToMany(this, a);
		    	setHoldingMultivaluedAssociationEndpoint(true);
		    }
		    if (associationEndpoint != null) {
		    	metaEntity.addAssociationEndpoint(associationEndpoint);
		    }
		}
	}

	public String getTimestampFormat() {
		return timestampFormat;
	}

	public void setTimestampFormat(String timestampFormat) {
		this.timestampFormat = timestampFormat;
	}

	public boolean isNotAutomaticallySet() {
		return isNotAutomaticallySet;
	}

	public void setNotAutomaticallySet(boolean isNotAutomaticallySet) {
		this.isNotAutomaticallySet = isNotAutomaticallySet;
	}

	public boolean isCreateUserField() {
		return isCreateUserField;
	}

	public boolean isModificationUserField() {
		return isModificationUserField;
	}

	public boolean isCreateTimestampField() {
		return isCreateTimestampField;
	}

	public boolean isModificationTimestampField() {
		return isModificationTimestampField;
	}
	
	public boolean isAutomaticallySetStringTimestamp() {
		return (isCreateTimestampField || isModificationTimestampField) && isStringField();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
