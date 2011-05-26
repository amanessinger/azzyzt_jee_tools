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

import org.azzyzt.jee.tools.mwe.identifiers.FieldNames;
import org.azzyzt.jee.tools.mwe.identifiers.ModelProperties;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;

public class DerivedModelBuilder {

	protected MetaModel masterModel;
	protected MetaModel targetModel;
	protected String targetPackageName;
	protected MetaStandardDefs std;

	public DerivedModelBuilder() { }
	
	public DerivedModelBuilder(MetaModel masterModel, String targetPackageName) {
		super();
		this.targetPackageName = targetPackageName;
		this.masterModel = masterModel;
		this.targetModel = new MetaModel(
				this.getClass().getSimpleName(), 
				masterModel.getProjectBaseName(), 
				masterModel.getLogger()
		);
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
		MetaDeclaredType genericEao = (MetaDeclaredType)masterModel.getProperty(ModelProperties.GENERIC_EAO);
		MetaField genericEaoField = new MetaField(target, FieldNames.EAO);
		genericEaoField.setFieldType(genericEao);
		genericEaoField.setModifiers(std.mod_private);
		genericEaoField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(genericEaoField);
		target.addReferencedForeignType(genericEao);
		target.setProperty(ModelProperties.GENERIC_EAO, genericEao);
	}

	protected void addEJBSessionContext(MetaClass target) {
		MetaDeclaredType sessionContext = std.javaxEjbSessionContext;
		MetaField sessionContextField = new MetaField(target, FieldNames.EJB_SESSION_CONTEXT);
		sessionContextField.setFieldType(sessionContext);
		sessionContextField.setModifiers(std.mod_private);
		sessionContextField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxAnnotationResource, target));
		target.addField(sessionContextField);
		target.addReferencedForeignType(sessionContext);
		target.setProperty(ModelProperties.EJB_SESSION_CONTEXT, sessionContext);
	}

	protected void addInvocationRegistryField(MetaClass target) {
		MetaDeclaredType invocationRegistry = (MetaDeclaredType)masterModel.getProperty(ModelProperties.INVOCATION_REGISTRY);
		MetaField invocationRegistryField = new MetaField(target, FieldNames.INVOCATION_REGISTRY);
		invocationRegistryField.setFieldType(invocationRegistry);
		invocationRegistryField.setModifiers(std.mod_private);
		invocationRegistryField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(invocationRegistryField);
		target.addReferencedForeignType(invocationRegistry);
		target.setProperty(ModelProperties.INVOCATION_REGISTRY, invocationRegistry);
	}

	protected void addConverterField(MetaClass target, MetaClass entity) {
		MetaDeclaredType converter = (MetaDeclaredType)entity.getProperty(ModelProperties.CONVERTER);
		MetaField converterField = new MetaField(target, FieldNames.CONVERTER);
		converterField.setFieldType(converter);
		converterField.setModifiers(std.mod_private);
		converterField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(converterField);
		target.addReferencedForeignType(converter);
		target.setProperty(ModelProperties.CONVERTER, converter);
	}

	protected void addValidAssociationPaths(MetaClass target) {
		MetaDeclaredType validAssociationPaths = (MetaDeclaredType)masterModel.getProperty(ModelProperties.VALID_ASSOCIATION_PATHS);
		MetaField validAssociationPathsField = new MetaField(target, FieldNames.VALID_ASSOCIATION_PATHS);
		validAssociationPathsField.setFieldType(validAssociationPaths);
		validAssociationPathsField.setModifiers(std.mod_private);
		validAssociationPathsField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(validAssociationPathsField);
		target.addReferencedForeignType(validAssociationPaths);
		target.setProperty(ModelProperties.VALID_ASSOCIATION_PATHS, validAssociationPaths);
	}

	protected void addTypeMetaInfoField(MetaClass target) {
		addTypeMetaInfoFieldImpl(target, true);
	}

	protected void addNonInjectedTypeMetaInfoField(MetaClass target) {
		addTypeMetaInfoFieldImpl(target, false);
	}

	private void addTypeMetaInfoFieldImpl(MetaClass target, boolean isInjected) {
		MetaDeclaredType tmi = (MetaDeclaredType)masterModel.getProperty(ModelProperties.TYPE_META_INFO);
		MetaField tmiField = new MetaField(target, FieldNames.TYPE_META_INFO);
		tmiField.setFieldType(tmi);
		tmiField.setModifiers(std.mod_private);
		if (isInjected) {
			tmiField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		}
		target.addField(tmiField);
		target.addReferencedForeignType(tmi);
		target.setProperty(ModelProperties.TYPE_META_INFO, tmi);
	}

	protected void addNonInjectedIdTranslatorField(MetaClass target) {
		MetaDeclaredType idTranslator = std.idTranslator;
		MetaField idTranslatorField = new MetaField(target, FieldNames.ID_TRANSLATOR);
		idTranslatorField.setFieldType(idTranslator);
		idTranslatorField.setModifiers(std.mod_private);
		target.addField(idTranslatorField);
		target.addReferencedForeignType(idTranslator);
		target.setProperty(ModelProperties.ID_TRANSLATOR, idTranslator);
	}

	protected void addFullServiceBeanField(MetaClass target, MetaClass entity) {
		MetaDeclaredType svcBean = (MetaDeclaredType)entity.getProperty(ModelProperties.SVC_FULL_BEAN);
		MetaField svcBeanField = new MetaField(target, FieldNames.SVC_FULL_BEAN);
		svcBeanField.setFieldType(svcBean);
		svcBeanField.setModifiers(std.mod_private);
		svcBeanField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(svcBeanField);
		target.addReferencedForeignType(svcBean);
		target.setProperty(ModelProperties.SVC_FULL_BEAN, svcBean);
	}

	protected void addRestrictedServiceBeanField(MetaClass target, MetaClass entity) {
		MetaDeclaredType svcBean = (MetaDeclaredType)entity.getProperty(ModelProperties.SVC_RESTRICTED_BEAN);
		MetaField svcBeanField = new MetaField(target, FieldNames.SVC_RESTRICTED_BEAN);
		svcBeanField.setFieldType(svcBean);
		svcBeanField.setModifiers(std.mod_private);
		svcBeanField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(svcBeanField);
		target.addReferencedForeignType(svcBean);
		target.setProperty(ModelProperties.SVC_RESTRICTED_BEAN, svcBean);
	}

	protected void addHttpServletResponseField(MetaClass target) {
		MetaField httpServletresponseField = new MetaField(target, FieldNames.HTTP_SERVLET_RESPONSE);
		httpServletresponseField.setFieldType(std.javaxServletHttpHttpServletResponse);
		httpServletresponseField.setModifiers(std.mod_private);
		httpServletresponseField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxWsRsCoreContext, target));
		target.addField(httpServletresponseField);
	}

	protected void addModifyMultiBeanField(MetaClass target) {
		MetaDeclaredType svcBean = (MetaDeclaredType)masterModel.getProperty(ModelProperties.MODIFY_MULTI_BEAN);
		MetaField svcBeanField = new MetaField(target, FieldNames.SVC_BEAN);
		svcBeanField.setFieldType(svcBean);
		svcBeanField.setModifiers(std.mod_private);
		svcBeanField.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbEJB, target));
		target.addField(svcBeanField);
		target.addReferencedForeignType(svcBean);
		target.setProperty(ModelProperties.MODIFY_MULTI_BEAN, svcBean);
	}

	protected void addTransactionRollbackHandler(MetaClass target) {
		MetaClass transactionRollbackHandler = (MetaClass)masterModel.getProperty(ModelProperties.TRANSACTION_ROLLBACK_HANDLER);
		/*
		 * We'd like to simply construct an annotation with the handler's class as value, 
		 * but unfortunately we don't have the class object, because the class was synthesized 
		 * instead of analyzed. We could try to load it, but instead we simply construct the 
		 * annotation as string and set extra annotations. 
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("@Interceptors(");
		sb.append(transactionRollbackHandler.getSimpleName());
		sb.append(".class)\n");
		target.setExtraClassAnnotationsText(sb.toString());
		target.addReferencedForeignType(std.javaxInterceptorInterceptors);
		target.addReferencedForeignType(transactionRollbackHandler);
	}

}