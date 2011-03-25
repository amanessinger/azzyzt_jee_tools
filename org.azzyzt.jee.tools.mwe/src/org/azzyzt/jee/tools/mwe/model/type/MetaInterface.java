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


public class MetaInterface extends MetaDeclaredType {

	public static MetaInterface forName(String packageName, String simpleName) {
		MetaInterface result = getOrConstruct(null, packageName, simpleName);
		return result;
	}
	
	public static MetaInterface forType(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String simpleName = clazz.getSimpleName();
		MetaInterface result = getOrConstruct(clazz, packageName, simpleName);
		return result;
	}

	protected static MetaInterface getOrConstruct(Class<?> clazz,
			String packageName, String simpleName) {
		MetaType metaType = MetaTypeRegistry.metaTypeForName(createFqName(packageName, simpleName));
		MetaInterface result = (MetaInterface)metaType;
		if (result == null) {
			result = new MetaInterface(clazz, packageName, simpleName);
			result.postConstructionAnalysis();
		}
		return result;
	}
	
	protected MetaInterface(Class<?> clazz, String packageName, String simpleName) {
		super(clazz, packageName, simpleName);
	}

	@Override
	public void postConstructionAnalysis() {
		super.postConstructionAnalysis();
		Class<?> clazz = getClazz();
		if (isTarget() && clazz != null) {
			analyzeMethods(clazz);
		}
	}

	@Override
	public int compareTo(MetaDeclaredType other) {
		return getFqName().compareTo(other.getFqName());
	}
}
