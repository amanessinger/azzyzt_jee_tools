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

import java.lang.reflect.Modifier;

public class MetaModifiers {

	private boolean isAbstract = false;
	private boolean isFinal = false;
	private boolean isInterface = false;
	private boolean isNative = false;
	private boolean isPrivate = false;
	private boolean isProtected = false;
	private boolean isPublic = false;
	private boolean isStatic = false;
	private boolean isStrict = false;
	private boolean isSynchronized = false;
	private boolean isTransient = false;
	private boolean isVolatile = false;

	public MetaModifiers() { }
	
	MetaModifiers(int reflected) {
		if (Modifier.isAbstract(reflected)) isAbstract = true;
		if (Modifier.isFinal(reflected)) isFinal = true;
		if (Modifier.isInterface(reflected)) isInterface = true;
		if (Modifier.isNative(reflected)) isNative = true;
		if (Modifier.isPrivate(reflected)) isPrivate = true;
		if (Modifier.isProtected(reflected)) isProtected = true;
		if (Modifier.isPublic(reflected)) isPublic = true;
		if (Modifier.isStatic(reflected)) isStatic = true;
		if (Modifier.isStrict(reflected)) isStrict = true;
		if (Modifier.isSynchronized(reflected)) isSynchronized = true;
		if (Modifier.isTransient(reflected)) isTransient = true;
		if (Modifier.isVolatile(reflected)) isVolatile = true;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isStrict() {
		return isStrict;
	}

	public void setStrict(boolean isStrict) {
		this.isStrict = isStrict;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public boolean isVolatile() {
		return isVolatile;
	}

	public void setVolatile(boolean isVolatile) {
		this.isVolatile = isVolatile;
	}

	@Override
	public String toString() {
		String sep = "";
		StringBuilder sb = new StringBuilder();
		if (isPrivate)      { sb.append(sep); sb.append("private"); sep = " "; }
		if (isProtected)    { sb.append(sep); sb.append("protected"); sep = " "; }
		if (isPublic)       { sb.append(sep); sb.append("public"); sep = " "; }
		if (isAbstract)     { sb.append(sep); sb.append("abstract"); sep = " "; }
		if (isInterface)    { sb.append(sep); sb.append("interface"); sep = " "; }
		if (isNative)       { sb.append(sep); sb.append("native"); sep = " "; }
		if (isStatic)       { sb.append(sep); sb.append("static"); sep = " "; }
		if (isFinal)        { sb.append(sep); sb.append("final"); sep = " "; }
		if (isStrict)       { sb.append(sep); sb.append("strict"); sep = " "; }
		if (isSynchronized) { sb.append(sep); sb.append("synchronized"); sep = " "; }
		if (isTransient)    { sb.append(sep); sb.append("transient"); sep = " "; }
		if (isVolatile)     { sb.append(sep); sb.append("volatile"); sep = " "; }
		return sb.toString();
	}
}

