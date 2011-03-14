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
	private boolean isHoldingMultivaluedAssociationEndpoint = false;
	private boolean isNotAutomaticallySet = true;
	private MetaAssociationEndpoint associationEndpoint = null;

	private String createTimestampFormat;
	private String modificationTimestampFormat;
	
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
					createTimestampFormat = tst.format();
					if (createTimestampFormat == null || createTimestampFormat.isEmpty()) {
						String msg = "Field attributed with @CreateTimestamp needs timestamp format";
						throw new ToolError(msg);
					}
				} else if (isDateField() || isCalendarField()) 
				{
					// nothing to do
				} else {
					String msg = "Field attributed with @CreateTimestamp must be a String, Date or Calendar";
					throw new ToolError(msg);
				}
				metaEntity.setCreateTimestampField(this);
				isNotAutomaticallySet = false;
			} else if (fa.annotationType().equals(ModificationTimestamp.class)) {
				// we need this to be a String, Calendar or Date
				if (isStringField()) {
					// needs a format attribute
					ModificationTimestamp tst = (ModificationTimestamp)fa;
					modificationTimestampFormat = tst.format();
					if (modificationTimestampFormat == null || modificationTimestampFormat.isEmpty()) {
						String msg = "Field attributed with @ModificationTimestamp needs timestamp format";
						throw new ToolError(msg);
					}
				} else if (isDateField() || isCalendarField()) 
				{
					// nothing to do
				} else {
					String msg = "Field attributed with @ModificationTimestamp must be a String, Date or Calendar";
					throw new ToolError(msg);
				}
				metaEntity.setModificationTimestampField(this);
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

	public String getModificationTimestampFormat() {
		return modificationTimestampFormat;
	}

	public void setModificationTimestampFormat(String modificationTimestampFormat) {
		this.modificationTimestampFormat = modificationTimestampFormat;
	}

	public String getCreateTimestampFormat() {
		return createTimestampFormat;
	}

	public void setCreateTimestampFormat(String createTimestampFormat) {
		this.createTimestampFormat = createTimestampFormat;
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

}
