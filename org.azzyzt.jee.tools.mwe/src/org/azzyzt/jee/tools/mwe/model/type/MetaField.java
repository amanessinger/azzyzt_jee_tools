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
	
	/* 
	 * Either use lists of actual annotation instances to put on getters/setters, or synthesize
	 * the annotation texts and set that. You could even do both.
	 */
	private List<MetaAnnotationInstance> getterMetaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
	private String getterMetaAnnotationText = "";
	private List<MetaAnnotationInstance> setterMetaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
	private String setterMetaAnnotationText = "";
    
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

	public String getGetterMetaAnnotationText() {
		return getterMetaAnnotationText;
	}

	public void setGetterMetaAnnotationText(String getterMetaAnnotationText) {
		this.getterMetaAnnotationText = getterMetaAnnotationText;
	}

	public String getSetterMetaAnnotationText() {
		return setterMetaAnnotationText;
	}

	public void setSetterMetaAnnotationText(String setterMetaAnnotationText) {
		this.setterMetaAnnotationText = setterMetaAnnotationText;
	}
	
}
