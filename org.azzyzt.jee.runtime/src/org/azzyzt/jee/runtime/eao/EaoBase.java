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


public abstract class EaoBase {
	
	

    public abstract EntityManager getEm();
    
	public void flush() {
	    getEm().flush();
	}

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
	 * Another version needed in cases when we are definite about the classes,
	 * but have no way to prove it to the compiler. Used for the "delete" part
	 * of "storeDelete()".
	 * 
	 * Order changed because of otherwise identical erasure.
	 * 
	 * @param id
	 * @param clazz
	 * @return
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

	public <I, T extends EntityBase<I>> void persist(T entity) {
	    if (entity.likelyHasId()) {
	        getEm().merge(entity);
	    } else {
	        getEm().persist(entity);
	    }
	}

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
	 * @param id
	 * @param clazz
	 * @throws EntityNotFoundException
	 */
	public void delete(Object id, Class<?> clazz)
	throws EntityNotFoundException {
		Object e = findOrFail(id, clazz);
		getEm().remove(e);
	}

	public <I, T extends EntityBase<I>> void deleteByName(Class<T> clazz, String nameWc) {
		Query q = getEm().createQuery("delete from "+clazz.getSimpleName()+" where name like :nameWc");
		q.setParameter("nameWc", nameWc);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public <I, T extends EntityBase<I>> List<T> all(Class<T> clazz) {
		Query q = getEm().createQuery("select c from "+clazz.getSimpleName()+" c");
		List<T> result = q.getResultList();
		return result;
	}
	
	public <I, T extends EntityBase<I>> List<T> list(QuerySpec qs, Class<T> clazz, TypeMetaInfoInterface tmi) 
	    throws InvalidFieldException, AccessDeniedException, QuerySyntaxException, NotYetImplementedException 
	{
		QueryBuilder<I, T> qb = new QueryBuilder<I, T>(getEm(), qs, clazz, tmi);
		TypedQuery<T> tq = qb.build();
		List<T> result = tq.getResultList();
		
		return result;
	}
	
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
