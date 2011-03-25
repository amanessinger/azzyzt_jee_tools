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

package org.azzyzt.jee.tools.mwe.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.type.MetaArray;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEnum;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;
import org.azzyzt.jee.tools.mwe.model.type.MetaTypeRegistry;

/**
 * Annotations are strange beasts. They are kind of interfaces, 
 * but when we encounter one of them sitting on a class or method, etc,
 * it is actually an instance of an annotation, because it has
 * specific values for its properties. Thus we model annotations as
 * MetaTypes having instances <strong>inside</strong> the model.
 * 
 * What we actually attach to other types are not the annotations but
 * their instances.
 */
public class MetaAnnotation extends MetaDeclaredType {
	
	private Map<String, MetaAnnotationElement> elements = new HashMap<String, MetaAnnotationElement>();
	private boolean isStaticallyAnalyzed = false;

	public static List<MetaAnnotationInstance> toInstances(MetaAnnotatable annotated, Annotation[] annotations) {
		List<MetaAnnotationInstance> result = new ArrayList<MetaAnnotationInstance>();
		if (annotations != null && annotations.length > 0) {
			if (!(annotated instanceof MetaAnnotation && !((MetaAnnotation)annotated).isTarget())) {
				for (Annotation ann : annotations) {
					Class<? extends Annotation> annotationType = ann.annotationType();
					MetaAnnotation metaAnnotation = MetaAnnotation.forType(annotationType);
					if (!metaAnnotation.isStaticallyAnalyzed()) {
						metaAnnotation.postConstructionAnalysis(annotationType);
					}
					annotated.addReferencedForeignType(metaAnnotation);
					MetaAnnotationInstance mai = new MetaAnnotationInstance(metaAnnotation, annotated);
					for (MetaAnnotationElement mae : metaAnnotation.getElements()) {
						String elementName = mae.getName();
						Method elementMethod = mae.getMethod();
						MetaType elementMetaType = mae.getMetaType();
						try {
							Object elementRawValue = elementMethod.invoke(ann);
							// Do some special casing to skip empty elements, unquoted strings, etc, and import enums and array element types
							if (elementMetaType instanceof MetaArray) {
								// avoids 'cascade='
								Object[] a = (Object[])elementRawValue;
								if (a == null || a.length == 0) continue;
								mai.setElement(elementName, elementRawValue);
							} else if (elementMetaType instanceof MetaEnum) {
								// avoids 'fetch=EAGER' instead of 'fetch=TetchType.EAGER'
								mai.setElement(elementName, ((MetaEnum) elementMetaType).getMetaEnumConstantByName(elementRawValue.toString()));
								annotated.addReferencedForeignType(elementMetaType);
							} else if (elementRawValue != null && !(elementRawValue instanceof String && ((String)elementRawValue).isEmpty())) {
								// just use it, if it's neither null nor the empty string
								mai.setElement(elementName, elementRawValue);
							} else {
								// skip the element completely.
								// System.out.println(elementName+"="+elementRawValue.toString()+" - SKIPPED");
								continue;
							}
							// System.out.println(elementName+"="+elementRawValue.toString());
						} catch (SecurityException e) {
							throw new ToolError(e);
						} catch (IllegalArgumentException e) {
							throw new ToolError(e);
						} catch (IllegalAccessException e) {
							throw new ToolError(e);
						} catch (InvocationTargetException e) {
							throw new ToolError(e);
						}
					}
					result.add(mai);
				}
			}
		}
		return result;
	}
	
	public static MetaAnnotation forName(String packageName, String simpleName) {
		MetaAnnotation result = getOrConstruct(null, packageName, simpleName);
		return result;
	}
	
	public static MetaAnnotation forType(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String simpleName = clazz.getSimpleName();
		MetaAnnotation result = getOrConstruct(clazz, packageName, simpleName);
		return result;
	}

	public static MetaAnnotation getOrConstruct(Class<?> clazz,
			String packageName, String simpleName) {
		MetaType metaType = MetaTypeRegistry.metaTypeForName(createFqName(packageName, simpleName));
		MetaAnnotation result = (MetaAnnotation)metaType;
		if (result == null) {
			result = new MetaAnnotation(clazz, packageName, simpleName);
			result.postConstructionAnalysis();
		}
		return result;
	}
	
	protected MetaAnnotation(Class<?> clazz, String packageName, String simpleName) {
		super(clazz, packageName, simpleName);
	}

	private void postConstructionAnalysis(Class<?> clazz) {		
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method m : declaredMethods) {
			MetaAnnotationElement mae = new MetaAnnotationElement();
			String elementName = m.getName();
			mae.setName(elementName);
			mae.setMethod(m);
			// System.out.println("@"+clazz.getSimpleName()+" - "+elementName);
			try {
				Type elementType = m.getGenericReturnType();
				MetaType elementMetaType = MetaType.forType(elementType);
				mae.setMetaType(elementMetaType);
			} catch (SecurityException e) {
				throw new ToolError(e);
			} catch (IllegalArgumentException e) {
				throw new ToolError(e);
			}
			addElement(elementName, mae);
		}
		setStaticallyAnalyzed(true);
	}
	
	@Override
	public void postConstructionAnalysis() {
		super.postConstructionAnalysis();
		Class<?> clazz = getClazz();
		if (clazz != null) {
			postConstructionAnalysis(clazz);
		}
	}

	public void addElement(String elementName, MetaAnnotationElement mae) {
		this.elements.put(elementName, mae);
	}

	public Set<String> getElementNames() {
		return elements.keySet();
	}
	
	public Collection<MetaAnnotationElement> getElements() {
		return elements.values();
	}
	
	public boolean hasElement(String elementName) {
		return elements.containsKey(elementName);
	}
	
	@Override
	public int compareTo(MetaDeclaredType other) {
		return getFqName().compareTo(other.getFqName());
	}

	public boolean isStaticallyAnalyzed() {
		return isStaticallyAnalyzed;
	}

	public void setStaticallyAnalyzed(boolean isStaticallyAnalyzed) {
		this.isStaticallyAnalyzed = isStaticallyAnalyzed;
	}

}
