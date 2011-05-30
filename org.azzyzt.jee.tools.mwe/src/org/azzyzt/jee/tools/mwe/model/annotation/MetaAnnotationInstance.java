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

package org.azzyzt.jee.tools.mwe.model.annotation;

import java.util.Properties;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class MetaAnnotationInstance {

	private MetaAnnotation metaAnnotation;
	
	private Properties elements = new Properties();
	private Properties unquotedlyValuedElements = new Properties();
	private MetaAnnotatable annotated;
	
	public MetaAnnotationInstance(MetaAnnotation metaAnnotation, MetaAnnotatable annotated) {
		this.metaAnnotation = metaAnnotation;
		this.annotated = annotated;
		if (annotated != null) {
			annotated.addReferencedForeignType(metaAnnotation);
		}
	}
	
	public void setElement(String element, Object value) {
		if (!metaAnnotation.hasElement(element)) {
			throw new ToolError("Can't set element "+element+" unsupported by "+metaAnnotation);
		}
		unquotedlyValuedElements.put(element, value);
		if (value instanceof String) {
			elements.put(element, "\""+value+"\"");
		} else if (value.getClass().isArray()) {
			// Fix ``cascade=´´
			Object[] a = (Object[])value;
			StringBuilder sb = new StringBuilder("{");
			String sep = "";
			for (Object o : a) {
				sb.append(sep);
				Class<? extends Object> clazz = o.getClass();
				if (clazz.isEnum()) {
					sb.append(clazz.getSimpleName()+"."+o.toString());
					MetaType metaEnumType = MetaType.forType(clazz);
					if (annotated == null) {
						throw new ToolError("Can't add referenced type to annotated object, because it has not been set yet");
					}
					annotated.addReferencedForeignType(metaEnumType);
				} else {
					sb.append(o.toString());
				}
				sep = ", ";
			}
			sb.append("}");
			elements.put(element, sb.toString());
		} else {
			elements.put(element, value);
		}
	}
	
	public boolean hasElement(String element) {
		return metaAnnotation.hasElement(element) && elements.containsKey(element);
	}
	
	public Object getValue(String element) {
		if (!hasElement(element)) {
			throw new ToolError("Element "+element+" is unsupported by "+metaAnnotation);
		}
		return elements.get(element);
	}

	public Object getRawValue(String element) {
		if (!hasElement(element)) {
			throw new ToolError("Element "+element+" is unsupported by "+metaAnnotation);
		}
		return unquotedlyValuedElements.get(element);
	}

	public String getName() {
		return metaAnnotation.getSimpleName();
	}
	
	public String getFqName() {
		return metaAnnotation.getFqName();
	}
	
	public boolean isNotEmpty() {
		return !(elements.isEmpty());
	}
	
	public Set<String> getElementNames() {
		return elements.stringPropertyNames();
	}

	public Properties getElements() {
		return elements;
	}

	public Properties getUnquotedlyValuedElements() {
		return unquotedlyValuedElements;
	}

	public void dropElement(String element) {
		if (!metaAnnotation.hasElement(element)) {
			throw new ToolError("Can't drop element "+element+" unsupported by "+metaAnnotation);
		}
		if (elements.containsKey(element)) {
			elements.remove(element);
		}
	}

	public MetaAnnotatable getAnnotated() {
		return annotated;
	}

	public void setAnnotated(MetaAnnotatable annotated) {
		this.annotated = annotated;
		annotated.addReferencedForeignType(metaAnnotation);
	}

	public MetaAnnotation getMetaAnnotation() {
		return metaAnnotation;
	}
}
