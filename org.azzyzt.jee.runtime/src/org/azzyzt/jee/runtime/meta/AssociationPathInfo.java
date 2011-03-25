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

package org.azzyzt.jee.runtime.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.runtime.exception.InvalidFieldException;

public class AssociationPathInfo {
	
	private List<AssociationInfo> associationInfos = new ArrayList<AssociationInfo>();
	
	private Set<String> associationInfosSeen = new HashSet<String>();
	
	private String fieldSelector;

	public AssociationPathInfo(String fieldSelector) {
		super();
		this.fieldSelector = fieldSelector;
	}

	public String getFieldSelector() {
		return fieldSelector;
	}

	public List<AssociationInfo> getAssociationInfos() {
		return associationInfos;
	}

	public void addAssociationInfo(AssociationInfo ai) 
		throws InvalidFieldException 
	{
		if (associationInfosSeen.contains(ai.getId())) {
			throw new InvalidFieldException("Circular join, path fragment "+ai.getFieldSelector()+" repeated");
		}
		this.associationInfos.add(ai);
		associationInfosSeen.add(ai.getId());
	}

}
