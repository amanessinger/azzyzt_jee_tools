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

package org.azzyzt.jee.tools.mwe.builder;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class DtoBaseModelBuilder extends DerivedModelBuilder implements Builder {

	public DtoBaseModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}
	
	/* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.Builder#build()
	 */
	public MetaModel build() {
		
		MetaClass dtoBase = null;
		List<String> dtoFQNames = new ArrayList<String>();
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "dto");
			String simpleName = me.getSimpleName();
			simpleName += "Dto";
			if (dtoBase == null) {
				dtoBase = MetaClass.forName(packageName, "DtoBase");
				dtoBase.setModifiers(std.mod_public);
				masterModel.setProperty("dtoBase", dtoBase);
			}
			dtoFQNames.add(packageName+"."+simpleName);
		}
		
		/*
		 * Just make sure it's included. The actual annotation is added textually via a property.
		 */
		dtoBase.addReferencedForeignType(std.javaxXmlBindAnnotationXmlSeeAlso);
		
		/*
		 * Construct text for the property. This is dirty but easy.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("@XmlSeeAlso({");
		String separator = "\n\t";
		for (String dtoFQName : dtoFQNames) {
			sb.append(separator);
			separator = ",\n\t";
			
			sb.append(dtoFQName);
			sb.append(".class");
		}
		sb.append("})");
		dtoBase.setProperty("xmlSeeAlsoInstance", sb.toString());
		
		targetModel.follow(dtoBase.getPackageName());
		targetModel.addMetaDeclaredTypeIfTarget(dtoBase);
		
		return targetModel;
	}
}
