package org.azzyzt.jee.runtime.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.azzyzt.jee.runtime.exception.AccessDeniedException;
import org.azzyzt.jee.runtime.exception.InvalidFieldException;
import org.azzyzt.jee.runtime.exception.ThisCantHappen;

public abstract class EntityMetaInfoBase {

	protected static Map<Class<?>, Set<String>> fieldsByClass = new HashMap<Class<?>, Set<String>>();
	protected static Map<Class<?>, Map<String, Class<?>>> fieldsTypesByClassAndName = new HashMap<Class<?>, Map<String, Class<?>>>();
	protected static Map<Class<?>, Map<String, AssociationInfo>> associationsByClassAndPath = new HashMap<Class<?>, Map<String,AssociationInfo>>();
	
	protected static Map<Class<?>, String> createUserFieldByClass = new HashMap<Class<?>, String>();
	protected static Map<Class<?>, String> modificationUserFieldByClass = new HashMap<Class<?>, String>();
	
	public EntityMetaInfoBase() {
		super();
	}

	public abstract ValidAssociactionPathsInterface getValidPaths();
	
	public void fieldVerification(Class<?> clazz, String name)
		throws InvalidFieldException, AccessDeniedException 
	{
		if (fieldsByClass.get(clazz) == null) {
			throw new AccessDeniedException();
		} else if (getValidPaths().contains(clazz, name)) {
			return;
		} else if (isAssociationPath(name)) {
			verifyAssociationPath(clazz, name);
			return;
		} else if (!fieldsByClass.get(clazz).contains(name)) {
	        throw new InvalidFieldException(name);
	    }
	}

	public boolean isAssociationPath(String name) {
		return name.contains(".");
	}
	
	private void verifyAssociationPath(Class<?> clazz, String name) 
		throws InvalidFieldException 
	{
		List<String> pathFragments = new ArrayList<String>();
		String fieldSelector;
		String[] parts = name.split("\\.");

		if (parts.length < 2) {
			throw new InvalidFieldException(name);
		}
		
		for (int i = 0; i < parts.length - 1; i++) {
			String part = parts[i];
			if (part == null || part.isEmpty()) {
				throw new InvalidFieldException(name);
			}
			pathFragments.add(part);
		}
		fieldSelector = parts[parts.length - 1];
		
		AssociationPathInfo api = new AssociationPathInfo(fieldSelector);
		
		Class<?> sourceClazz = clazz;
		for (String pathFragment : pathFragments) {
			if (!associationsByClassAndPath.get(sourceClazz).containsKey(pathFragment)) {
				throw new InvalidFieldException(name+" ("+sourceClazz.getSimpleName()+"."+pathFragment+")");
			}
			AssociationInfo ai = associationsByClassAndPath.get(sourceClazz).get(pathFragment);
			api.addAssociationInfo(ai);
			sourceClazz = ai.getJoinTo();
		}
		if (!fieldsByClass.get(sourceClazz).contains(fieldSelector)) {
			throw new InvalidFieldException(name+" ("+sourceClazz.getSimpleName()+"."+fieldSelector+")");
		}
		
		getValidPaths().add(clazz, name, api);
	}
	
	/**
	 * Returns the type of either the field of "clazz" or of the field pointed to from "clazz" via path "name"
	 * 
	 * @param clazz
	 * @param name
	 * @return type of either the field of "clazz" or of the field pointed to from "clazz" via path "name"
	 * @throws InvalidFieldException
	 * @throws AccessDeniedException
	 */
	public Class<?> getFieldType(Class<?> clazz, String name)
		throws InvalidFieldException, AccessDeniedException 
	{
	    if (fieldsByClass.get(clazz) == null) {
	        throw new AccessDeniedException();
	    }
	    
	    // may be overridden if "name" is an association path
	    TargetFieldSelector tfs = new TargetFieldSelector(clazz, name);
	    tfs = resolveAssociations(tfs);
	    return fieldsTypesByClassAndName.get(tfs.getTargetEntity()).get(tfs.getFieldSelector());
	}

	private TargetFieldSelector resolveAssociations(TargetFieldSelector tfs)
		throws InvalidFieldException 
	{
		Class<?> targetEntity = tfs.getTargetEntity();
	    String fieldSelector = tfs.getFieldSelector();
	    
	    if (isAssociationPath(fieldSelector)) {
	    	if (!getValidPaths().contains(targetEntity, fieldSelector)) {
	    		// must have been validated
	    		throw new ThisCantHappen();
	    	}
	    	AssociationPathInfo api = getValidPaths().get(targetEntity, fieldSelector);
	    	List<AssociationInfo> associationInfos = api.getAssociationInfos();
	    	AssociationInfo lastAiInPath = associationInfos.get(associationInfos.size() - 1);
			targetEntity = lastAiInPath.getJoinTo();
			fieldSelector = api.getFieldSelector(); 
	    }
	    if (!fieldsByClass.get(targetEntity).contains(fieldSelector)) {
	        throw new InvalidFieldException(fieldSelector);
	    }
	    tfs = new TargetFieldSelector(targetEntity, fieldSelector);
		return tfs;
	}

}