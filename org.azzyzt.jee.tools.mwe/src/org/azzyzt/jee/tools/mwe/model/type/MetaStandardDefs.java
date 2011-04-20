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

package org.azzyzt.jee.tools.mwe.model.type;

import java.util.ArrayList;
import java.util.List;

import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotation;

public class MetaStandardDefs {
	
	public final MetaModifiers mod_private;
	public final MetaModifiers mod_protected;
	public final MetaModifiers mod_public;

	public final MetaModifiers mod_private_static;
	public final MetaModifiers mod_protected_static;
	public final MetaModifiers mod_public_static;

	public final MetaModifiers mod_protected_abstract;
	public final MetaModifiers mod_public_abstract;
	
	public final MetaModifiers mod_private_static_final;
	public final MetaModifiers mod_protected_static_final;
	public final MetaModifiers mod_public_static_final;

	public final MetaBuiltin meta_boolean;
	public final MetaBuiltin meta_byte;
	public final MetaBuiltin meta_char;
	public final MetaBuiltin meta_short;
	public final MetaBuiltin meta_int;
	public final MetaBuiltin meta_long;
	public final MetaBuiltin meta_float;
	public final MetaBuiltin meta_double;
	
	public final MetaClass meta_Boolean;
	public final MetaClass meta_Byte;
	public final MetaClass meta_Character;
	public final MetaClass meta_Short;
	public final MetaClass meta_Integer;
	public final MetaClass meta_Long;
	public final MetaClass meta_Float;
	public final MetaClass meta_Double;
	
	public final MetaClass meta_String;

	public final MetaClass meta_Throwable;
	
	public final MetaClass javaLangReflectInvocationTargetException;
	public final MetaClass javaLangReflectMethod;

	public final MetaInterface javaUtilList;
	public final MetaClass javaUtilArrayList;
	public final MetaInterface javaUtilSet;
	public final MetaClass javaUtilHashSet;
	public final MetaInterface javaUtilMap;
	public final MetaClass javaUtilHashMap;
	public final MetaClass javaUtilCalendar;
	public final MetaClass javaUtilDate;
	
	public final MetaInterface javaIoSerializable;
	
	public final MetaClass javaTextSimpleDateFormat;
	
	public final MetaAnnotation javaxEjbLocal;
	public final MetaAnnotation javaxEjbRemote;
	public final MetaAnnotation javaxEjbLocalBean;
	public final MetaAnnotation javaxEjbStateless;
	public final MetaAnnotation javaxEjbSingleton;
	public final MetaAnnotation javaxEjbLock;
	public final MetaEnum javaxEjbLockType;
	public final MetaAnnotation javaxEjbEJB;

	public final MetaAnnotation javaxAnnotationResource;

	public final MetaAnnotation javaxInterceptorAroundInvoke;
	public final MetaAnnotation javaxInterceptorInterceptor;
	public final MetaInterface javaxInterceptorInvocationContext;
	public final MetaAnnotation javaxInterceptorInterceptors;
	
	public final MetaInterface javaxPersistenceEntityManager;
	public final MetaAnnotation javaxPersistencePersistenceContext;
	public final MetaAnnotation javaxPersistenceEntity;
	public final MetaAnnotation javaxPersistenceId;
	public final MetaAnnotation javaxPersistenceOneToOne;
	public final MetaAnnotation javaxPersistenceManyToOne;
	public final MetaAnnotation javaxPersistenceOneToMany;
	public final MetaAnnotation javaxPersistenceManyToMany;
	public final MetaAnnotation javaxPersistencePrePersist;
	public final MetaAnnotation javaxPersistencePreUpdate;
	
	public final MetaAnnotation javaxJwsWebService;
	
	public final MetaAnnotation javaxXmlBindAnnotationXmlRootElement;
	public final MetaAnnotation javaxXmlBindAnnotationXmlElementWrapper;
	public final MetaAnnotation javaxXmlBindAnnotationXmlElement;
	public final MetaAnnotation javaxXmlBindAnnotationXmlSeeAlso;
	
	public final MetaClass javaxWsRsCoreApplication;
	public final MetaAnnotation javaxWsRsApplicationPath;
	public final MetaAnnotation javaxWsRsPath;
	public final MetaAnnotation javaxWsRsGET;
	public final MetaAnnotation javaxWsRsPOST;
	public final MetaAnnotation javaxWsRsConsumes;
	public final MetaAnnotation javaxWsRsProduces;
	public final MetaAnnotation javaxWsRsPathParam;
	public final MetaAnnotation javaxWsRsQueryParam;
	public final MetaAnnotation javaxWsRsCoreProvider;
	public final MetaClass stringListWrapper;
	
	public final MetaInterface javaxServletHttpHttpServletResponse;
	public final MetaAnnotation javaxWsRsCoreContext;
	public final MetaClass javaxWsRsCoreMediaType;
	public final MetaInterface javaxWsRsExtExceptionMapperThrowable;

	public final MetaClass eaoBase;
	public final MetaClass entityBase;
	public final MetaClass entityListenerBase;
	public final MetaInterface invocationRegistryInterface;
	public final MetaClass InvocationRegistryBase;
	public final MetaInterface javaxTransactionTransactionSynchronizationRegistry;
	public final MetaClass multiObjectSaver;
	public final MetaInterface converterRawInterface;
	
	public final MetaClass accessDeniedException;
	public final MetaClass entityNotFoundException;
	public final MetaClass entityInstantiationException;
	public final MetaClass invalidArgumentException;
	public final MetaClass invalidIdException;
	public final MetaClass invalidFieldException;
	public final MetaClass querySyntaxException;
	public final MetaClass notYetImplementedException;
	public final MetaClass restDelegatorBase;
	public final MetaClass orderByClause;
	public final MetaInterface typeMetaInfo;
	public final MetaClass typeMetaInfoBase;
	public final MetaClass querySpec;
	public final MetaClass associationInfo;
	public final MetaClass associationPathInfo;
	public final MetaClass siteAdapterBase;
	public final MetaInterface siteAdapterInterface;
	public final MetaEnum requiredSelectionType;
	public final MetaEnum joinType;
	public final MetaEnum dateFieldType;
	public final MetaClass validAssociationPathsBase;
	public final MetaInterface validAssociationPathsInterface;
	public final MetaClass exceptionToSuccessMapper;
	

	public MetaStandardDefs() {
		
		mod_private = new MetaModifiers();
		mod_private.setPrivate(true);
		
		mod_protected = new MetaModifiers();
		mod_protected.setProtected(true);
		
		mod_public = new MetaModifiers();
		mod_public.setPublic(true);
		
		mod_private_static = new MetaModifiers();
		mod_private_static.setPrivate(true);
		mod_private_static.setStatic(true);
		
		mod_protected_static = new MetaModifiers();
		mod_protected_static.setProtected(true);
		mod_protected_static.setStatic(true);
		
		mod_public_static = new MetaModifiers();
		mod_public_static.setPublic(true);
		mod_public_static.setStatic(true);
		
		mod_protected_abstract = new MetaModifiers();
		mod_protected_abstract.setProtected(true);
		mod_protected_abstract.setAbstract(true);
		
		mod_public_abstract = new MetaModifiers();
		mod_public_abstract.setPublic(true);
		mod_public_abstract.setAbstract(true);
		
		mod_private_static_final = new MetaModifiers();
		mod_private_static_final.setPrivate(true);
		mod_private_static_final.setStatic(true);
		mod_private_static_final.setFinal(true);
		
		mod_protected_static_final = new MetaModifiers();
		mod_protected_static_final.setProtected(true);
		mod_protected_static_final.setStatic(true);
		mod_protected_static_final.setFinal(true);
		
		mod_public_static_final = new MetaModifiers();
		mod_public_static_final.setPublic(true);
		mod_public_static_final.setStatic(true);
		mod_public_static_final.setFinal(true);
		
		meta_boolean = MetaBuiltin.forName("boolean");
		meta_byte = MetaBuiltin.forName("byte");
		meta_char = MetaBuiltin.forName("char");
		meta_short = MetaBuiltin.forName("short");
		meta_int = MetaBuiltin.forName("int");
		meta_long = MetaBuiltin.forName("long");
		meta_float = MetaBuiltin.forName("float");
		meta_double = MetaBuiltin.forName("double");
		
		meta_Boolean = MetaClass.forType(java.lang.Boolean.class);
		meta_Byte = MetaClass.forType(java.lang.Byte.class);
		meta_Character = MetaClass.forType(java.lang.Character.class);
		meta_Short = MetaClass.forType(java.lang.Short.class);
		meta_Integer = MetaClass.forType(java.lang.Integer.class);
		meta_Long = MetaClass.forType(java.lang.Long.class);
		meta_Float = MetaClass.forType(java.lang.Float.class);
		meta_Double = MetaClass.forType(java.lang.Double.class);
		meta_String = MetaClass.forType(java.lang.String.class);

		meta_Throwable = MetaClass.forType(Throwable.class);
		
		javaLangReflectInvocationTargetException = MetaClass.forType(java.lang.reflect.InvocationTargetException.class);
		javaLangReflectMethod = MetaClass.forType(java.lang.reflect.Method.class);

		javaUtilList = MetaInterface.forType(java.util.List.class);
		javaUtilArrayList = MetaClass.forType(java.util.ArrayList.class);
		javaUtilSet = MetaInterface.forType(java.util.Set.class);
		javaUtilHashSet = MetaClass.forType(java.util.HashSet.class);
		javaUtilMap = MetaInterface.forType(java.util.Map.class);
		javaUtilHashMap = MetaClass.forType(java.util.HashMap.class);
		javaUtilCalendar = MetaClass.forType(java.util.Calendar.class);
		javaUtilDate = MetaClass.forType(java.util.Date.class);

		javaIoSerializable = MetaInterface.forType(java.io.Serializable.class);
		
		javaTextSimpleDateFormat = MetaClass.forType(java.text.SimpleDateFormat.class);

		javaxEjbLocal = MetaAnnotation.forType(javax.ejb.Local.class);
		javaxEjbRemote = MetaAnnotation.forType(javax.ejb.Remote.class);
		javaxEjbLocalBean = MetaAnnotation.forType(javax.ejb.LocalBean.class);
		javaxEjbStateless = MetaAnnotation.forType(javax.ejb.Stateless.class);
		javaxEjbSingleton = MetaAnnotation.forType(javax.ejb.Singleton.class);
		javaxEjbLock = MetaAnnotation.forType(javax.ejb.Lock.class);
		javaxEjbLockType = MetaEnum.forType(javax.ejb.LockType.class);
		javaxEjbEJB = MetaAnnotation.forType(javax.ejb.EJB.class);
		
		javaxAnnotationResource = MetaAnnotation.forType(javax.annotation.Resource.class);
		
		javaxInterceptorAroundInvoke = MetaAnnotation.forType(javax.interceptor.AroundInvoke.class);
		javaxInterceptorInterceptor = MetaAnnotation.forType(javax.interceptor.Interceptor.class);
		javaxInterceptorInterceptors = MetaAnnotation.forType(javax.interceptor.Interceptors.class);
		javaxInterceptorInvocationContext = MetaInterface.forType(javax.interceptor.InvocationContext.class);
		
		javaxPersistenceEntityManager = MetaInterface.forType(javax.persistence.EntityManager.class);
		javaxPersistencePersistenceContext = MetaAnnotation.forType(javax.persistence.PersistenceContext.class);
		javaxPersistenceEntity = MetaAnnotation.forType(javax.persistence.Entity.class);
		javaxPersistenceId = MetaAnnotation.forType(javax.persistence.Id.class);
		javaxPersistenceOneToOne = MetaAnnotation.forType(javax.persistence.OneToOne.class);
		javaxPersistenceManyToOne = MetaAnnotation.forType(javax.persistence.ManyToOne.class);
		javaxPersistenceOneToMany = MetaAnnotation.forType(javax.persistence.OneToMany.class);
		javaxPersistenceManyToMany = MetaAnnotation.forType(javax.persistence.ManyToMany.class);
		javaxPersistencePrePersist = MetaAnnotation.forType(javax.persistence.PrePersist.class);
		javaxPersistencePreUpdate = MetaAnnotation.forType(javax.persistence.PreUpdate.class);
		
		javaxJwsWebService = MetaAnnotation.forType(javax.jws.WebService.class);
		
		javaxXmlBindAnnotationXmlRootElement = MetaAnnotation.forType(javax.xml.bind.annotation.XmlRootElement.class);
		javaxXmlBindAnnotationXmlElementWrapper = MetaAnnotation.forType(javax.xml.bind.annotation.XmlElementWrapper.class);
		javaxXmlBindAnnotationXmlElement = MetaAnnotation.forType(javax.xml.bind.annotation.XmlElement.class);
		javaxXmlBindAnnotationXmlSeeAlso = MetaAnnotation.forType(javax.xml.bind.annotation.XmlSeeAlso.class);
		
		javaxWsRsCoreApplication = MetaClass.forType(javax.ws.rs.core.Application.class);
		javaxWsRsApplicationPath = MetaAnnotation.forType(javax.ws.rs.ApplicationPath.class);
		javaxWsRsPath = MetaAnnotation.forType(javax.ws.rs.Path.class);
		javaxWsRsGET = MetaAnnotation.forType(javax.ws.rs.GET.class);
		javaxWsRsPOST = MetaAnnotation.forType(javax.ws.rs.POST.class);
		javaxWsRsConsumes = MetaAnnotation.forType(javax.ws.rs.Consumes.class);
		javaxWsRsProduces = MetaAnnotation.forType(javax.ws.rs.Produces.class);
		javaxWsRsPathParam = MetaAnnotation.forType(javax.ws.rs.PathParam.class);
		javaxWsRsQueryParam = MetaAnnotation.forType(javax.ws.rs.QueryParam.class);
		javaxWsRsCoreMediaType = MetaClass.forType(javax.ws.rs.core.MediaType.class);
		javaxServletHttpHttpServletResponse = MetaInterface.forType(javax.servlet.http.HttpServletResponse.class);
		javaxWsRsCoreContext = MetaAnnotation.forType(javax.ws.rs.core.Context.class);
		javaxWsRsCoreProvider = MetaAnnotation.forType(javax.ws.rs.ext.Provider.class);
		javaxWsRsExtExceptionMapperThrowable = MetaInterface.forType(javax.ws.rs.ext.ExceptionMapper.class);
		List<MetaType> metaArgumentTypes = new ArrayList<MetaType>();
		metaArgumentTypes.add(meta_Throwable);
		javaxWsRsExtExceptionMapperThrowable.setMetaArgumentTypes(metaArgumentTypes);
		stringListWrapper = MetaClass.forType(org.azzyzt.jee.runtime.dto.StringListWrapper.class);
		
		eaoBase = MetaClass.forType(org.azzyzt.jee.runtime.eao.EaoBase.class);
		entityBase = MetaClass.forType(org.azzyzt.jee.runtime.entity.EntityBase.class);
		entityListenerBase = MetaClass.forType(org.azzyzt.jee.runtime.entity.EntityListenerBase.class);
		invocationRegistryInterface = MetaInterface.forType(org.azzyzt.jee.runtime.meta.InvocationRegistryInterface.class);
		InvocationRegistryBase = MetaClass.forType(org.azzyzt.jee.runtime.meta.InvocationRegistryBase.class);
		javaxTransactionTransactionSynchronizationRegistry = MetaInterface.forType(javax.transaction.TransactionSynchronizationRegistry.class);
		multiObjectSaver = MetaClass.forType(org.azzyzt.jee.runtime.eao.MultiObjectSaver.class);
		converterRawInterface = MetaInterface.forType(org.azzyzt.jee.runtime.conv.ConverterRawInterface.class);
		
		accessDeniedException = MetaClass.forType(org.azzyzt.jee.runtime.exception.AccessDeniedException.class);
		entityNotFoundException = MetaClass.forType(org.azzyzt.jee.runtime.exception.EntityNotFoundException.class);
		entityInstantiationException = MetaClass.forType(org.azzyzt.jee.runtime.exception.EntityInstantiationException.class);
		invalidArgumentException = MetaClass.forType(org.azzyzt.jee.runtime.exception.InvalidArgumentException.class);
		invalidIdException = MetaClass.forType(org.azzyzt.jee.runtime.exception.InvalidIdException.class);
		invalidFieldException = MetaClass.forType(org.azzyzt.jee.runtime.exception.InvalidFieldException.class);
		querySyntaxException = MetaClass.forType(org.azzyzt.jee.runtime.exception.QuerySyntaxException.class);
		notYetImplementedException = MetaClass.forType(org.azzyzt.jee.runtime.exception.NotYetImplementedException.class);
		restDelegatorBase = MetaClass.forType(org.azzyzt.jee.runtime.service.RESTDelegatorBase.class);
		orderByClause = MetaClass.forType(org.azzyzt.jee.runtime.dto.query.OrderByClause.class);
		typeMetaInfo = MetaInterface.forType(org.azzyzt.jee.runtime.meta.TypeMetaInfoInterface.class);
		typeMetaInfoBase = MetaClass.forType(org.azzyzt.jee.runtime.meta.TypeMetaInfoBase.class);
		querySpec = MetaClass.forType(org.azzyzt.jee.runtime.dto.query.QuerySpec.class);
		associationInfo = MetaClass.forType(org.azzyzt.jee.runtime.meta.AssociationInfo.class);
		associationPathInfo = MetaClass.forType(org.azzyzt.jee.runtime.meta.AssociationPathInfo.class);
		siteAdapterBase = MetaClass.forType(org.azzyzt.jee.runtime.service.SiteAdapterBase.class);
		siteAdapterInterface = MetaInterface.forType(org.azzyzt.jee.runtime.util.SiteAdapterInterface.class);
		requiredSelectionType = MetaEnum.forType(org.azzyzt.jee.runtime.meta.RequiredSelectionType.class);
		joinType = MetaEnum.forType(org.azzyzt.jee.runtime.meta.JoinType.class);
		dateFieldType = MetaEnum.forType(org.azzyzt.jee.runtime.entity.DateFieldType.class);
		validAssociationPathsBase = MetaClass.forType(org.azzyzt.jee.runtime.meta.ValidAssociationPathsBase.class);
		validAssociationPathsInterface = MetaInterface.forType(org.azzyzt.jee.runtime.meta.ValidAssociactionPathsInterface.class);
		exceptionToSuccessMapper = MetaClass.forType(org.azzyzt.jee.runtime.service.ExceptionToSuccessMapper.class);
	}
}
