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

package org.azzyzt.jee.tools.mwe.builder;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotatable;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;
import org.azzyzt.jee.tools.mwe.model.association.MetaManyToMany;
import org.azzyzt.jee.tools.mwe.model.association.MetaManyToOne;
import org.azzyzt.jee.tools.mwe.model.association.MetaOneToMany;
import org.azzyzt.jee.tools.mwe.model.association.MetaOneToOne;
import org.azzyzt.jee.tools.mwe.model.type.MetaBuiltin;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaCollection;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntityField;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class DtoModelBuilder extends DerivedModelBuilder implements Builder {

	public DtoModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}
	
	/* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.Builder#build()
	 */
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "dto");
			String simpleName = me.getSimpleName();
			simpleName += "Dto";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			me.setProperty("dto", target);
			MetaAnnotationInstance mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlRootElement, target);
			mai.setElement("name", me.getSimpleName().toLowerCase());
			target.addMetaAnnotationInstance(mai);
			target.setModifiers(std.mod_public);
			target.addInterface(std.javaIoSerializable);
			target.setSerialVersion(true);
			target.addReferencedForeignType(std.javaIoSerializable);
			
			List<MetaAssociationEndpoint> maes = me.getAssociationEndpoints();
			for (MetaAssociationEndpoint mae : maes) {
				MetaEntity targetEntity = (MetaEntity)mae.getTargetEntity();
				if (targetEntity.isCombinedId()) {
					target.addReferencedForeignType(targetEntity.getCombinedIdType());
				}
			}
			
			for (MetaField mf : me.getFields()) {
				MetaEntityField mef = (MetaEntityField)mf;
				
				if (mef.isInternal()) continue;
				
				MetaType metaEntityFieldType = mef.getFieldType();
				
				// normally a DTO has identical fields
				MetaType dtoFieldType = metaEntityFieldType;
				String dtoFieldName = mef.getName();

				if (mef.isIdField()) {
					target.setProperty("idFieldType", dtoFieldType);
				}
				
				// just in case we need to initialize the field
				String initializer = null;
				
				// it may be an association though
				MetaAssociationEndpoint mae = mef.getAssociationEndpoint();
				List<MetaAnnotationInstance> dtoGetterMais = new ArrayList<MetaAnnotationInstance>();
				if (mae != null) {
					MetaEntity targetEntity;
					if (mae instanceof MetaOneToOne || mae instanceof MetaManyToOne) {
						/*
						 *  Send the ID only, assuming that o2o was introduced 
						 *  because one part is expensive or infrequently used
						 */
						targetEntity = (MetaEntity)metaEntityFieldType;
						/*
						 *  TODO look at HwAuftragInventar (ManyToMany) and document how to use @Embedded for complex IDs
						 */
						dtoFieldType = targetEntity.getIdField().getFieldType();
						dtoFieldName += "Id";
					} else if (mae instanceof MetaOneToMany || mae instanceof MetaManyToMany) {
						/*
						 * We generate the DTO field regardless of fetch type. Outgoing the converter will
						 * fill the collection only for FetchType.EAGER, otherwise null it out 
						 */
						MetaCollection metaEntityCollectionType = (MetaCollection)metaEntityFieldType;
						targetEntity = (MetaEntity)metaEntityCollectionType.getEntityMemberType();
						MetaType idType = targetEntity.getIdField().getFieldType();
						if (idType instanceof MetaBuiltin) {
							idType = ((MetaBuiltin)idType).getBoxedType();
						}
						target.addReferencedForeignType(std.javaUtilList);
						List<MetaType> argumentTypes = new ArrayList<MetaType>();
						argumentTypes.add(idType);
						dtoFieldType = MetaCollection.forNameAndMetaTypes("java.util", "List", std.javaUtilList, argumentTypes);
						dtoFieldName += "Ids";
						initializer = "new ArrayList<"+idType.getShortName()+">()";
						target.addReferencedForeignType(std.javaUtilArrayList);
						
						// we serialize arrays with wrappers in XML
						mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlElementWrapper, (MetaAnnotatable)null);
						mai.setElement("name", mf.getName());
						dtoGetterMais.add(mai);
						mai = new MetaAnnotationInstance(std.javaxXmlBindAnnotationXmlElement, (MetaAnnotatable)null);
						mai.setElement("name", "id");
						dtoGetterMais.add(mai);
					} else {
						throw new ToolError("MetaAssociation "+mae+" has an unsupported type");
					}
				}
				
				// now generate the field
				MetaField dtoField = new MetaField(target, dtoFieldName);
				dtoField.setModifiers(std.mod_private);
				dtoField.setFieldType(dtoFieldType);
				dtoField.setInitializer(initializer);
				for (MetaAnnotationInstance dtoGetterMai : dtoGetterMais) {
					dtoGetterMai.setAnnotated(dtoField);
					dtoField.addGetterMetaAnnotationInstance(dtoGetterMai);
				}
				target.addReferencedForeignType(dtoFieldType);
			}
		}

		return targetModel;
	}
}
