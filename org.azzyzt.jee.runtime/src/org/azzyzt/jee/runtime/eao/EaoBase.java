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

package org.azzyzt.jee.runtime.eao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.azzyzt.jee.runtime.dto.query.OrderByClause;
import org.azzyzt.jee.runtime.dto.query.QuerySpec;
import org.azzyzt.jee.runtime.entity.EntityBase;
import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.exception.EntityInstantiationException;
import org.azzyzt.jee.runtime.exception.EntityNotFoundException;
import org.azzyzt.jee.runtime.exception.InvalidFieldException;
import org.azzyzt.jee.runtime.exception.InvalidIdException;
import org.azzyzt.jee.runtime.exception.NotYetImplementedException;
import org.azzyzt.jee.runtime.exception.QuerySyntaxException;
import org.azzyzt.jee.runtime.meta.TypeMetaInfoInterface;
import org.azzyzt.jee.runtime.util.QueryBuilder;


/**
 * The abstract base class of the generated generic entity access objects. 
 * The generated generic EAO is a stateless session bean, and its only 
 * purpose is to be a target for injection. All interaction with JPA happens here. 
 */
public abstract class EaoBase {
	
	

    /**
     * The <code>EntityManager</code> is injected into the concrete generated
     * child class. This abstract method lets the generic methods access the 
     * persistence context.
     * 
     * @return the <code>EntityManager</code>
     */
    public abstract EntityManager getEm();
    
	public void flush() {
	    getEm().flush();
	}

	/**
	 * Fetches an entity by ID or, if it does not exist, fails with an exception.
	 *  
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @return the entity
	 * @throws EntityNotFoundException
	 */
	public <I, T extends EntityBase<I>> T findOrFail(Class<T> clazz, I id)
	throws EntityNotFoundException 
	{
	    T e = getEm().find(clazz, id);
	    if (e == null) {
	        throw new EntityNotFoundException(clazz.getClass(), id.toString());
	    }
	    return e;
	}

	/**
	 * A raw version of {@link #findOrFail(Class, Object)} needed in cases when we are definite about the classes,
	 * but have no way to prove it to the compiler. Used for the "delete" part
	 * of "storeDelete()".
	 * 
	 * Order changed because of otherwise identical erasure.
	 * 
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @return the entity
	 * @throws EntityNotFoundException
	 */
	public Object findOrFail(Object id, Class<?> clazz)
	throws EntityNotFoundException 
	{
	    Object e = getEm().find(clazz, id);
	    if (e == null) {
	        throw new EntityNotFoundException(clazz.getClass(), id.toString());
	    }
	    return e;
	}

	/**
	 * Fetches an entity by ID or, if it does not exist, creates it.
	 * 
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @return the entity
	 * @throws EntityNotFoundException
	 * @throws EntityInstantiationException
	 */
	public <I, T extends EntityBase<I>> T findOrCreate(Class<T> clazz, I id)
	throws EntityNotFoundException, EntityInstantiationException 
	{
		T result = null;
	    if (EntityBase.couldBeIdValue(id)) {
	        result = getEm().find(clazz, id);
	    }
	    if (result == null) {
	        try {
				result = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new EntityInstantiationException(clazz);
			} catch (IllegalAccessException e) {
				throw new EntityInstantiationException(clazz);
			}
	        persist(result);
	    }
		return result;
	}

	/**
	 * Fetches an entity by ID or, if the ID is invalid or <code>null</code>, fails.
	 * 
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @return the entity
	 * @throws EntityNotFoundException
	 * @throws InvalidIdException
	 */
	public <I, T extends EntityBase<I>> T findOrInvalidId(Class<T> clazz, I id)
	throws EntityNotFoundException, InvalidIdException 
	{
		T result;
	    if (EntityBase.couldBeIdValue(id)) {
	        result = findOrFail(clazz, id);
	    } else {
	    	throw new InvalidIdException(id.toString());
	    }
		return result;
	}
	
	/**
	 * Fetches an entity by ID or, if not found, returns <code>null</code>.
	 * 
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @return the entity or <code>null</code>
	 * @throws InvalidIdException
	 */
	public <I, T extends EntityBase<I>> T findOrNull(Class<T> clazz, I id) 
	throws InvalidIdException
	{
		T result;
	    if (EntityBase.couldBeIdValue(id)) {
	        result = getEm().find(clazz, id);
	    } else {
	    	throw new InvalidIdException(id.toString());
	    }
		return result;
	}

	/**
	 * Stores an entity.
	 * 
	 * @param entity the entity
	 */
	public <I, T extends EntityBase<I>> void persist(T entity) {
	    if (entity.likelyHasId()) {
	        getEm().merge(entity);
	    } else {
	        getEm().persist(entity);
	    }
	}

	/**
	 * Deletes an entity of a given class and ID.
	 * 
	 * @param clazz an entity class
	 * @param id the ID of an entity
	 * @throws EntityNotFoundException
	 */
	public <I, T extends EntityBase<I>> void delete(Class<T> clazz, I id)
	throws EntityNotFoundException {
		T e = findOrFail(clazz, id);
		getEm().remove(e);
	}

	/**
	 * Another version needed in cases when we are definite about the classes,
	 * but have no way to prove it to the compiler. Used for the "delete" part
	 * of "storeDelete()".
	 * 
	 * Order changed because of otherwise identical erasure.
	 * 
	 * @param id the ID of an entity
	 * @param clazz an entity class
	 * @throws EntityNotFoundException
	 */
	public void delete(Object id, Class<?> clazz)
	throws EntityNotFoundException {
		Object e = findOrFail(id, clazz);
		getEm().remove(e);
	}

	/**
	 * Deletes all entities with a name matching a certain wildcard.
	 * 
	 * @param clazz an entity class
	 * @param nameWc a name wildcard string
	 */
	public <I, T extends EntityBase<I>> void deleteByName(Class<T> clazz, String nameWc) {
		Query q = getEm().createQuery("delete from "+clazz.getSimpleName()+" where name like :nameWc");
		q.setParameter("nameWc", nameWc);
		q.executeUpdate();
	}

	/**
	 * Returns all entities of a certain class
	 * 
	 * @param clazz an entity class
	 * @return a list of entities
	 */
	@SuppressWarnings("unchecked")
	public <I, T extends EntityBase<I>> List<T> all(Class<T> clazz) {
		Query q = getEm().createQuery("select c from "+clazz.getSimpleName()+" c");
		List<T> result = q.getResultList();
		return result;
	}
	
	/**
	 * Returns the result of a typed query. The query is built by a <code>QueryBuilder</code> from
	 * a <code>QuerySpec</code>. 
	 * 
	 * @param qs a query specification
	 * @param clazz an entity class
	 * @param tmi type meta information generated by Azzyzt JEE Tools
	 * @return a list of entities
	 * @throws InvalidFieldException
	 * @throws AccessDeniedException
	 * @throws QuerySyntaxException
	 * @throws NotYetImplementedException
	 */
	public <I, T extends EntityBase<I>> List<T> list(QuerySpec qs, Class<T> clazz, TypeMetaInfoInterface tmi) 
	    throws InvalidFieldException, AccessDeniedException, QuerySyntaxException, NotYetImplementedException 
	{
		QueryBuilder<I, T> qb = new QueryBuilder<I, T>(getEm(), qs, clazz, tmi);
		TypedQuery<T> tq = qb.build();
		List<T> result = tq.getResultList();
		
		return result;
	}
	
	/**
	 * Returns all entities of a certain class, ordered according to an <code>OrderByClause</code>.
	 * 
	 * @param clazz an entity class
	 * @param orderBy an <code>OrderByClause</code>
	 * @return an ordered list of all entities of the given class
	 */
	@SuppressWarnings("unchecked")
	public <I, T extends EntityBase<I>> List<T> allOrdered(Class<T> clazz, OrderByClause orderBy) { 
		// orderBy is expected to be validated
		Query q = getEm().createQuery("select c from "+clazz.getSimpleName()+" c order by c."
									  +orderBy.getFieldName()
									  +(orderBy.isAscending() ? " ASC" : " DESC"));
		List<T> result = q.getResultList();
		return result;
	}
	
}
