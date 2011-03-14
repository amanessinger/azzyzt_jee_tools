package org.azzyzt.jee.tools.mwe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class MetaModel {
	
	private static MetaModel currentModel = null;
	
	private Set<String> packagesToFollow = new HashSet<String>();
	private List<Class<?>> targetClazzes  = new ArrayList<Class<?>>();
	private Set<MetaDeclaredType> targetMetaDeclaredTypes = new HashSet<MetaDeclaredType>();
	private Set<MetaEntity> targetEntities = new HashSet<MetaEntity>();
	private boolean isIncludingMethods = true;
	private boolean isIncludingStaticFields = true;
	private Properties properties = new Properties(); // may be set by a synthesizing builder


    /**
     * Caution: the constructor automatically sets a model "current". Thus the pattern is,
     * to create a model, build it or add to it manually, and then to generate it,
     * before the next model is created.
     */
    public MetaModel() {
    	MetaModel.setCurrentModel(this);
    }
    
    public void follow(String packagePrefix) {
    	packagesToFollow.add(packagePrefix);
    }
    
    public boolean addMetaDeclaredTypeIfTarget(MetaDeclaredType mdt) {
    	boolean yesItIs = false;

    	Class<?> clazz = mdt.getClazz();
		if (clazz != null && targetClazzes.contains(clazz)) {
    		yesItIs = true;
    	} else {
	    	Iterator<String> iterator = packagesToFollow.iterator();
	    	while (iterator.hasNext()) {
	    		String prefix = iterator.next();
	    		if (mdt.getPackageName().startsWith(prefix)) {
	    			yesItIs = true;
	    			if (clazz != null) {
	    				targetClazzes.add(clazz);
	    			}
	    		}
	    	}
    	}

    	if (yesItIs) {
    		addTargetMetaDeclaredType(mdt);
    	}
    	return yesItIs;
    }
    
    private void addTargetMetaDeclaredType(MetaDeclaredType mdt) {
    	targetMetaDeclaredTypes.add(mdt);
    	if (mdt instanceof MetaEntity) {
    		targetEntities.add((MetaEntity)mdt);
    	}
    }
    
	public Set<MetaDeclaredType> getTargetMetaDeclaredTypes() {
		return targetMetaDeclaredTypes;
	}

	public void build(List<String> metaDeclaredTypeNames) {
	    for (String clazzName : metaDeclaredTypeNames) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(clazzName);
			} catch (ClassNotFoundException e) {
			    throw new ToolError(e);
			}
			targetClazzes.add(clazz);
			MetaType.forType(clazz);
	    }
	}

	public void excludeMethodsFromModel() {
		this.isIncludingMethods = false;
	}

	public boolean isIncludingMethods() {
		return isIncludingMethods;
	}

	public void excludeStaticFieldsFromModel() {
		this.isIncludingStaticFields = false;
	}
	
	public boolean isIncludingStaticFields() {
		return isIncludingStaticFields;
	}

	public Set<MetaEntity> getTargetEntities() {
		return targetEntities;
	}
	
	public static void setCurrentModel(MetaModel currentModel) {
		MetaModel.currentModel = currentModel;
	}

	public static MetaModel getCurrentModel() {
		if (currentModel == null) {
			throw new ToolError("The current model is not yet set");
		}
		return currentModel;
	}

	public void setProperty(Object key, Object value) {
		properties.put(key, value);
	}

	public Object getProperty(Object key) {
		return properties.get(key);
	}

	public Properties getProperties() {
		return properties;
	}

}
