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

package org.azzyzt.jee.tools.mwe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.generator.GeneratorOptions;
import org.azzyzt.jee.tools.mwe.model.type.MetaDeclaredType;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;
import org.azzyzt.jee.tools.mwe.util.Log;

public class MetaModel {
	
	public static final String MASTER_MODEL_NAME = "MASTER_MODEL_NAME";

	private static MetaModel currentModel = null;
	
	private Set<String> packagesToFollow = new HashSet<String>();
	private List<Class<?>> targetClazzes  = new ArrayList<Class<?>>();
	private Set<MetaDeclaredType> targetMetaDeclaredTypes = new HashSet<MetaDeclaredType>();
	private Set<MetaEntity> targetEntities = new HashSet<MetaEntity>();
	private boolean isIncludingMethods = true;
	private boolean isIncludingStaticFields = true;
	private Properties properties = new Properties(); // may be set by a synthesizing builder
	private Log logger;
	private String name;
	private String projectBaseName;
	private GeneratorOptions generatorOptions;

	public static MetaModel createMasterModel(String projectBaseName, Log logger) {
		return new MetaModel(MASTER_MODEL_NAME, projectBaseName, logger);
	}

    /**
     * Caution: the constructor automatically sets a model "current". Thus the pattern is,
     * to create a model, build it or add to it manually, and then to generate it,
     * before the next model is created.
     */
    public MetaModel(String name, String projectBaseName, Log logger) {
    	// We feed class names of the builders creating the models; clean up
    	this.name = name.replaceAll("ModelBuilder$", "Model").replaceAll("Builder$", "Model");
    	this.projectBaseName = projectBaseName;
    	this.logger = logger;
    	logger.debug("Creating model "+this.name);
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
    	logger.debug(mdt.getFqName()+" is target");
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
		Object value = properties.get(key);
		if (value == null) {
			String msg = this.getName()+": property "+key+" has no value";
			logger.error(msg);
			throw new ToolError(msg);
		}
		return value;
	}

	public Properties getProperties() {
		return properties;
	}

	public Log getLogger() {
		return logger;
	}

	public String getName() {
		return name;
	}

	public String getProjectBaseName() {
		return projectBaseName;
	}

	public void setGeneratorOptions(GeneratorOptions generatorOptions) {
		this.generatorOptions = generatorOptions;
	}

	public GeneratorOptions getGeneratorOptions() {
		return generatorOptions;
	}

}
