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

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;

public class DerivedModelBuilder {

	protected final MetaModel masterModel;
	protected final MetaModel targetModel;
	protected final String targetPackageName;
	protected final MetaStandardDefs std;

	public DerivedModelBuilder(MetaModel masterModel, String targetPackageName) {
		super();
		this.targetPackageName = targetPackageName;
		this.masterModel = masterModel;
		this.targetModel = new MetaModel(masterModel.getLogger());
		this.std = new MetaStandardDefs();
	}
	
	protected String derivePackageNameFromEntityAndFollowPackage(MetaEntity me, String suffix) {
		String packageName = derivePackageNameFromEntity(me, suffix);
		targetModel.follow(packageName);
		return packageName;
	}

	protected String derivePackageNameFromEntity(MetaEntity me, String suffix) {
		String packageName;
		if (targetPackageName == null) {
			packageName = me.getPackageName();
			String dottedSuffix = "."+suffix;
			if (packageName.endsWith(".entity")) {
				packageName = packageName.replaceAll("\\.entity$", dottedSuffix);
			} else {
				packageName += dottedSuffix;
			}
		} else {
			// TODO write a test case for a package name set via constructor
			packageName = targetPackageName;
		}
		return packageName;
	}

	protected void addGenericEaoField(MetaClass target) {
		MetaDeclaredType genericEao = (MetaDeclaredType)masterModel.getProperty("generic_eao");
		MetaField genericEaoField = new MetaField(target, "eao");
		genericEaoField.setFieldType(genericEao);
		genericEaoField.setModifiers(std.mod_private);
		genericEaoField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(genericEaoField);
		target.addReferencedForeignType(genericEao);
		target.setProperty("eao", genericEao);
	}

	protected void addInvocationRegistryField(MetaClass target) {
		MetaDeclaredType invocationRegistry = (MetaDeclaredType)masterModel.getProperty("invocation_registry");
		MetaField invocationRegistryField = new MetaField(target, "invocationRegistry");
		invocationRegistryField.setFieldType(invocationRegistry);
		invocationRegistryField.setModifiers(std.mod_private);
		invocationRegistryField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(invocationRegistryField);
		target.addReferencedForeignType(invocationRegistry);
		target.setProperty("invocationRegistry", invocationRegistry);
	}

	protected void addConverterField(MetaClass target, MetaClass entity) {
		MetaDeclaredType converter = (MetaDeclaredType)entity.getProperty("conv");
		MetaField converterField = new MetaField(target, "converter");
		converterField.setFieldType(converter);
		converterField.setModifiers(std.mod_private);
		converterField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(converterField);
		target.addReferencedForeignType(converter);
		target.setProperty("converter", converter);
	}

	protected void addValidAssociationPaths(MetaClass target) {
		MetaDeclaredType validAssociationPaths = (MetaDeclaredType)masterModel.getProperty("validAssociationPaths");
		MetaField validAssociationPathsField = new MetaField(target, "validAssociationPaths");
		validAssociationPathsField.setFieldType(validAssociationPaths);
		validAssociationPathsField.setModifiers(std.mod_private);
		validAssociationPathsField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(validAssociationPathsField);
		target.addReferencedForeignType(validAssociationPaths);
		target.setProperty("validAssociationPaths", validAssociationPaths);
	}

	protected void addTypeMetaInfoField(MetaClass target) {
		MetaDeclaredType tmi = (MetaDeclaredType)masterModel.getProperty("typeMetaInfo");
		MetaField tmiField = new MetaField(target, "tmi");
		tmiField.setFieldType(tmi);
		tmiField.setModifiers(std.mod_private);
		tmiField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(tmiField);
		target.addReferencedForeignType(tmi);
		target.setProperty("tmi", tmi);
	}

	protected void addFullServiceBeanField(MetaClass target, MetaClass entity) {
		MetaDeclaredType svcBean = (MetaDeclaredType)entity.getProperty("svcFullBean");
		MetaField svcBeanField = new MetaField(target, "svcFullBean");
		svcBeanField.setFieldType(svcBean);
		svcBeanField.setModifiers(std.mod_private);
		svcBeanField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(svcBeanField);
		target.addReferencedForeignType(svcBean);
		target.setProperty("svcFullBean", svcBean);
	}

	protected void addRestrictedServiceBeanField(MetaClass target, MetaClass entity) {
		MetaDeclaredType svcBean = (MetaDeclaredType)entity.getProperty("svcRestrictedBean");
		MetaField svcBeanField = new MetaField(target, "svcRestrictedBean");
		svcBeanField.setFieldType(svcBean);
		svcBeanField.setModifiers(std.mod_private);
		svcBeanField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(svcBeanField);
		target.addReferencedForeignType(svcBean);
		target.setProperty("svcRestrictedBean", svcBean);
	}

	protected void addHttpServletResponseField(MetaClass target) {
		MetaField httpServletresponseField = new MetaField(target, "httpServletResponse");
		httpServletresponseField.setFieldType(std.javaxServletHttpHttpServletResponse);
		httpServletresponseField.setModifiers(std.mod_private);
		httpServletresponseField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxWsRsCoreContext, target));
		target.addField(httpServletresponseField);
	}
}