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

import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.identifiers.PackageTails;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class DtoBaseModelBuilder extends DerivedModelBuilder implements Builder {

	public static final String CLASS_NAME = "Dto";
	public static final String ROOT_ELEMENT = "dtoes";

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
			String packageName = derivePackageNameFromEntity(me, PackageTails.DTO);
			String simpleName = me.getSimpleName();
			simpleName += DtoModelBuilder.CLASS_SUFFIX;
			if (dtoBase == null) {
				dtoBase = MetaClass.forName(packageName, CLASS_NAME);
				dtoBase.setModifiers(std.mod_public);
				masterModel.setProperty(ModelProperties.DTO_BASE, dtoBase);
			}
			String dtoFQName = packageName+"."+simpleName;
			dtoFQNames.add(dtoFQName);
			
			String convPackageName = derivePackageNameFromEntity(me, PackageTails.CONV);
			String simpleConvName = me.getSimpleName();
			simpleConvName += EntityDtoConverterModelBuilder.CLASS_SUFFIX;
			String convFQName = convPackageName+"."+simpleConvName;
			
			me.setProperty(ModelProperties.CONV_FQ_NAME, convFQName);
			me.setProperty(ModelProperties.DTO_FQ_NAME, dtoFQName);
		}
		
		/*
		 * Just make sure it's included. The actual annotation is added textually via a property.
		 */
		dtoBase.addReferencedForeignType(std.javaxXmlBindAnnotationXmlSeeAlso);
		dtoBase.addReferencedForeignType(std.javaxXmlBindAnnotationXmlRootElement);
		
		/*
		 * Construct text for the property. This is dirty but easy.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("@XmlRootElement(name=\""+ROOT_ELEMENT+"\")\n");
		sb.append("@XmlSeeAlso({");
		String separator = "\n\t";
		for (String dtoFQName : dtoFQNames) {
			sb.append(separator);
			separator = ",\n\t";
			
			sb.append(dtoFQName);
			sb.append(".class");
		}
		sb.append("\n})");
		dtoBase.setProperty(ModelProperties.DTO_BASE_CLASS_ANNOTATIONS, sb.toString());
		
		targetModel.follow(dtoBase.getPackageName());
		targetModel.addMetaDeclaredTypeIfTarget(dtoBase);
		
		return targetModel;
	}
}
