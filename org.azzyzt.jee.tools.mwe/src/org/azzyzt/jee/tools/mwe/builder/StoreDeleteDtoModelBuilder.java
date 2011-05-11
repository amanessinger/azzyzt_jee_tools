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
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaCollection;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class StoreDeleteDtoModelBuilder extends DerivedModelBuilder implements Builder {

	private static final String STORE_DELETE_DTO_NAME = "StoreDelete";
	private static final String DELETE_WRAPPER_DTO_NAME = "DeleteWrapper";
	private static final String DELETE_WRAPPER_ROOT_ELEMENT = "delete";
	private static final String STORE_WRAPPER_DTO_NAME = "StoreWrapper";
	private static final String STORE_WRAPPER_ROOT_ELEMENT = "store";

	public StoreDeleteDtoModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}
	
	/* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.Builder#build()
	 */
	public MetaModel build() {
		
		MetaAnnotationInstance mai;

		MetaClass dtoBase = (MetaClass)masterModel.getProperty(ModelProperties.DTO_BASE);

		MetaClass deleteWrapperDto = MetaClass.forName(dtoBase.getPackageName(), DELETE_WRAPPER_DTO_NAME);
		MetaClass storeWrapperDto = MetaClass.forName(dtoBase.getPackageName(), STORE_WRAPPER_DTO_NAME);

		List<MetaType> argumentTypes = new ArrayList<MetaType>();
		argumentTypes.add(dtoBase);
		MetaCollection listFieldType = MetaCollection.forNameAndMetaTypes("java.util", "List", std.javaUtilList, argumentTypes);

		// construct the getter annotation text
		StringBuilder sb = new StringBuilder();
		sb.append("@XmlElementWrapper(name=\""+DtoBaseModelBuilder.ROOT_ELEMENT+"\")\n");
		sb.append("@XmlElements({");
		String separator = "\n\t";
		for (MetaEntity me : masterModel.getTargetEntities()) {
			String packageName = derivePackageNameFromEntity(me, PackageTails.DTO);
			String simpleName = me.getSimpleName();
			String rootElementName = simpleName.toLowerCase();
			simpleName += DtoModelBuilder.CLASS_SUFFIX;
			String dtoFQName = packageName+"."+simpleName;
			
			sb.append(separator);
			separator = ",\n\t";
			sb.append("@XmlElement(name=\"");
			sb.append(rootElementName);
			sb.append("\", type=");
			sb.append(dtoFQName);
			sb.append(".class)");
		}
		sb.append("\n})\n");
		String getterAnnotationText = sb.toString();

		MetaClass[] wrappers = { deleteWrapperDto, storeWrapperDto };
		for (MetaClass wrapper : wrappers) {
			wrapper.setModifiers(std.mod_public);
			wrapper.addInterface(std.javaIoSerializable);
			wrapper.setSerialVersion(true);
			
			wrapper.addReferencedForeignType(std.javaUtilArrayList);
			wrapper.addReferencedForeignType(std.javaxXmlBindAnnotationXmlElementWrapper);
			wrapper.addReferencedForeignType(std.javaxXmlBindAnnotationXmlElements);
			wrapper.addReferencedForeignType(std.javaxXmlBindAnnotationXmlElement);

			MetaField listField = new MetaField(wrapper, DtoBaseModelBuilder.ROOT_ELEMENT);
			listField.setModifiers(std.mod_private);
			listField.setFieldType(listFieldType);
			listField.setInitializer("new ArrayList<"+dtoBase.getShortName()+">()");
			listField.setGetterMetaAnnotationText(getterAnnotationText);
			
			targetModel.follow(wrapper.getPackageName());
			targetModel.addMetaDeclaredTypeIfTarget(wrapper);
		}
		
		masterModel.setProperty(ModelProperties.DELETE_WRAPPER_DTO, deleteWrapperDto); 
		mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, deleteWrapperDto);
		mai.setElement("name", DELETE_WRAPPER_ROOT_ELEMENT);
		deleteWrapperDto.addMetaAnnotationInstance(mai);
		
		masterModel.setProperty(ModelProperties.STORE_WRAPPER_DTO, storeWrapperDto); 
		mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, storeWrapperDto);
		mai.setElement("name", STORE_WRAPPER_ROOT_ELEMENT);
		storeWrapperDto.addMetaAnnotationInstance(mai);
		
		/*
		 * Construct a DTO that can wrap two lists of DTOs, one for storing, one for deleting 
		 */
		
		MetaClass storeDeleteMultiDto = MetaClass.forName(dtoBase.getPackageName(), STORE_DELETE_DTO_NAME);
		storeDeleteMultiDto.setModifiers(std.mod_public);
		storeDeleteMultiDto.addInterface(std.javaIoSerializable);
		storeDeleteMultiDto.setSerialVersion(true);
		masterModel.setProperty(ModelProperties.STORE_DELETE_DTO, storeDeleteMultiDto); 
		mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, storeDeleteMultiDto);
		mai.setElement("name", STORE_DELETE_DTO_NAME.toLowerCase());
		storeDeleteMultiDto.addMetaAnnotationInstance(mai);

		MetaField deleteField = new MetaField(storeDeleteMultiDto, DELETE_WRAPPER_ROOT_ELEMENT);
		deleteField.setModifiers(std.mod_private);
		deleteField.setFieldType(deleteWrapperDto);
		deleteField.setInitializer("new "+deleteWrapperDto.getShortName()+"()");
		
		MetaField storeField = new MetaField(storeDeleteMultiDto, STORE_WRAPPER_ROOT_ELEMENT);
		storeField.setModifiers(std.mod_private);
		storeField.setFieldType(storeWrapperDto);
		storeField.setInitializer("new "+storeWrapperDto.getShortName()+"()");
		
		targetModel.follow(storeDeleteMultiDto.getPackageName());
		targetModel.addMetaDeclaredTypeIfTarget(storeDeleteMultiDto);
		
		return targetModel;
	}
}
