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

package org.azzyzt.jee.tools.mwe.model.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;


public class MetaClass extends MetaDeclaredType {
	
	private boolean isSerialVersionNeeded = false;
	
	private boolean isImplicitlyImported = false;
	
	private Set<MetaInterface> interfaces = new HashSet<MetaInterface>();
	
	private Map<String, MetaField> fields = new HashMap<String, MetaField>();

	public static MetaClass forName(String packageName, String simpleName) {
		MetaClass result = getOrConstruct(null, packageName, simpleName);
		return result;
	}
	
	public static MetaClass forType(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String simpleName = clazz.getSimpleName();
		MetaClass result = getOrConstruct(clazz, packageName, simpleName);
		return result;
	}

	protected static MetaClass getOrConstruct(Class<?> clazz, String packageName,
			String simpleName) {
		MetaClass result = (MetaClass)MetaTypeRegistry.metaTypeForName(createFqName(packageName, simpleName));
		if (result == null) {
			result = new MetaClass(clazz, packageName, simpleName);
			result.postConstructionAnalysis();
		}
		return result;
	}
	
	protected MetaClass(Class<?> clazz, String packageName, String simpleName) {
		super(clazz, packageName, simpleName);
		if (packageName.equals("java.lang")) {
			setImplicitlyImported();
		}
	}
	
	@Override
	public void postConstructionAnalysis() {
		super.postConstructionAnalysis();
		MetaModel model = MetaModel.getCurrentModel();
		Class<?> clazz = getClazz();
		if (isTarget() && clazz != null) {
			analyzeInterfaces(clazz);
			analyzeFields(clazz);
			if (model.isIncludingMethods()) {
				analyzeMethods(clazz);
			}
		}
	}

	public void addField(MetaField mf) {
		fields.put(mf.getName(), mf);
	}

	public MetaField newMetaField(String fieldName) {
		return new MetaField(this, fieldName);
	}
	
	private void analyzeInterfaces(Class<?> clazz) {
		Type[] types = clazz.getGenericInterfaces();
		for (Type t : types) {
			MetaInterface metaInterface = (MetaInterface)MetaInterface.forType(t);
			addInterface(metaInterface);
			addReferencedForeignType(metaInterface);
		}
	}
	
	public void analyzeFields(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field fld : fields) {
			if (Modifier.isStatic(fld.getModifiers()) 
					&& ! MetaModel.getCurrentModel().isIncludingStaticFields()) {
				continue;
			}
			analyzeField(fld);
		}
	}

	public MetaField analyzeField(Field fld) {
		String fieldName = fld.getName();
		
		MetaField mf = newMetaField(fieldName);
		Type type = fld.getGenericType();
		MetaType fieldType = MetaType.forType(type);
		mf.setFieldType(fieldType);
		
		int mod = fld.getModifiers();
		mf.setModifiers(mod);
	
		mf.processAnnotations(fld);
		return mf;
	}

	public List<MetaField> getFields() {
		ArrayList<MetaField> result = new ArrayList<MetaField>(fields.values());
		Collections.sort(result);
		return result;
	}

	public boolean hasField(String name) {
		return fields.containsKey(name);
	}
	
	public MetaField getFieldByName(String name) {
		if (hasField(name)) {
			return fields.get(name);
		}
		throw new ToolError("Class "+getSimpleName()+" has no field "+name);
	}
	
	@Override
	public int compareTo(MetaDeclaredType other) {
		return getFqName().compareTo(other.getFqName());
	}

	public void addInterface(MetaInterface theInterface) {
		this.interfaces.add(theInterface);
		addReferencedForeignType(theInterface);
	}

	public Set<MetaInterface> getInterfaces() {
		return interfaces;
	}
	
	public boolean isImplementingInterfaces() {
		return !(interfaces.isEmpty());
	}

	public void setSerialVersion(boolean isSerialVersionNeeded) {
		this.isSerialVersionNeeded = isSerialVersionNeeded;
	}

	public boolean isSerialVersionNeeded() {
		return isSerialVersionNeeded;
	}

	@Override
	public boolean isImplicitlyImported() {
		return isImplicitlyImported;
	}

	private void setImplicitlyImported() {
		this.isImplicitlyImported = true;
	}

}
