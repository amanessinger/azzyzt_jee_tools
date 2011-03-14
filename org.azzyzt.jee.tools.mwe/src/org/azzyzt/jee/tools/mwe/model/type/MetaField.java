package org.azzyzt.jee.tools.mwe.model.type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotatable;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotation;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public class MetaField implements Comparable<MetaField>, MetaAnnotatable {

	private MetaFieldId id;
    private String name;
    private MetaDeclaredType ownerMdt;
    private MetaType fieldType;
    private MetaModifiers modifiers; 
	private List<MetaAnnotationInstance> metaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
	private List<MetaAnnotationInstance> getterMetaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
	private List<MetaAnnotationInstance> setterMetaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
    
	// TODO Add field initializers. Use MetaValue for that. 
	
    // fields that may be set by a synthesizing builder
    private String initializer;
	private Properties properties = new Properties();
    
    public MetaField(MetaDeclaredType ownerMdt, String fieldName) {
    	this.ownerMdt = ownerMdt;
        this.name = fieldName;
        this.id = new MetaFieldId(ownerMdt, fieldName);
        if (ownerMdt instanceof MetaClass) {
        	((MetaClass)ownerMdt).addField(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getUcName() {
    	return StringUtils.ucFirst(name);
    }

    public MetaDeclaredType getOwnerMdt() {
		return ownerMdt;
	}

	public MetaFieldId getId() {
		return id;
	}

	public void setFieldType(MetaType fieldType) {
		this.fieldType = fieldType;
		if (fieldType instanceof MetaDeclaredType) {
			MetaDeclaredType mdt = (MetaDeclaredType)fieldType;
			ownerMdt.addReferencedForeignType(mdt);
		} else if (fieldType instanceof MetaCollection) {
			ownerMdt.addReferencedForeignType(((MetaCollection) fieldType).getMetaRawType());
		}
	}

	public MetaType getFieldType() {
		return fieldType;
	}

	public MetaModifiers getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = new MetaModifiers(modifiers);
	}
	
	public void setModifiers(MetaModifiers modifiers) {
		this.modifiers = modifiers;
	}
	
	@Override
	public String toString() {
		return "MetaField [id=" + id + "]";
	}

	@Override
	public void setMetaAnnotationInstances(List<MetaAnnotationInstance> metaAnnotationInstances) {
		this.metaAnnotationInstances = metaAnnotationInstances;
	}

	@Override
	public void addMetaAnnotationInstance(MetaAnnotationInstance instance) {
		this.metaAnnotationInstances.add(instance);
	}
	
	@Override
	public List<MetaAnnotationInstance> getMetaAnnotationInstances() {
		return metaAnnotationInstances;
	}

	public void processAnnotations(Field fld) {
		setMetaAnnotationInstances(MetaAnnotation.toInstances(this, fld.getAnnotations()));
	}

	@Override
	public int compareTo(MetaField other) {
		return this.getName().compareTo(other.getName());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaField other = (MetaField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}

	public String getInitializer() {
		return initializer;
	}

	@Override
	public void addReferencedForeignType(MetaType referencedForeignType) {
		ownerMdt.addReferencedForeignType(referencedForeignType);
	}

	public void addGetterMetaAnnotationInstance(MetaAnnotationInstance instance) {
		this.getterMetaAnnotationInstances.add(instance);
	}
	
	public List<MetaAnnotationInstance> getGetterMetaAnnotationInstances() {
		return getterMetaAnnotationInstances;
	}

	public List<MetaAnnotationInstance> getSetterMetaAnnotationInstances() {
		return setterMetaAnnotationInstances;
	}

	public void addSetterMetaAnnotationInstance(MetaAnnotationInstance instance) {
		this.setterMetaAnnotationInstances.add(instance);
	}

	public boolean isCalendarField() {
		return getFieldType().equals(MetaType.getStandardtypes().javaUtilCalendar);
	}

	public boolean isDateField() {
		return getFieldType().equals(MetaType.getStandardtypes().javaUtilDate);
	}

	public boolean isStringField() {
		return getFieldType().equals(MetaType.getStandardtypes().meta_String);
	}
	
}
