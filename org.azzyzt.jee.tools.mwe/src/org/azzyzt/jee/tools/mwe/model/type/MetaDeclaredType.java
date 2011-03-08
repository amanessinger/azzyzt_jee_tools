package org.azzyzt.jee.tools.mwe.model.type;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotatable;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotation;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.util.StringUtils;

public abstract class MetaDeclaredType extends MetaType implements Comparable<MetaDeclaredType>, MetaAnnotatable {

	private String fqName;
	private String packageName;
	private String simpleName;
	private Class<?> clazz;
    private MetaModifiers modifiers;
	private Set<MetaDeclaredType> referencedForeignTypes = new HashSet<MetaDeclaredType>();
	private Map<String, MetaMethod> methods = new HashMap<String, MetaMethod>();
	private boolean isTarget;
	private List<MetaAnnotationInstance> metaAnnotationInstances = new ArrayList<MetaAnnotationInstance>();
	private List<MetaType> metaArgumentTypes;
	private String typeParameterString;
	private String shortTypeParameterString;
	
	public static String createFqName(String packageName, String simpleName) {
		return packageName+"."+simpleName;
	}

	protected MetaDeclaredType(Class<?> clazz, String packageName, String simpleName) {
		super(createFqName(packageName, simpleName));
		setClazz(clazz);
		setPackageName(packageName);
		setSimpleName(simpleName);
		setFqName(createFqName(packageName, simpleName));
		setTarget(MetaModel.getCurrentModel().addMetaDeclaredTypeIfTarget(this));
	}

	public void postConstructionAnalysis() {
		if (clazz == null) return;
		
		setModifiers(clazz.getModifiers());
		setMetaAnnotationInstances(MetaAnnotation.toInstances(this, clazz.getAnnotations()));
		Type superclass = clazz.getGenericSuperclass();
		if (superclass != null && !superclass.equals(Object.class)) {
			superMetaClass = MetaType.forType(superclass);
			addReferencedForeignType(superMetaClass);
			superMetaClass.addSubtype(this);
		}
	}

	public Class<?> getClazz() {
		return clazz;
	}

	protected void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public MetaModifiers getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = new MetaModifiers(modifiers);
	}
	
	public void setModifiers(MetaModifiers modifiers) {
		this.modifiers = modifiers;
	}
	
	public String getFqName() {
	    return fqName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getLcFirstSimpleName() {
		return StringUtils.lcFirst(simpleName);
	}

	private void setFqName(String fqName) {
		this.fqName = fqName;
	}

	private void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	private void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public List<MetaDeclaredType> getReferencedForeignTypes() {
		List<MetaDeclaredType> result = new ArrayList<MetaDeclaredType>(referencedForeignTypes);
		Collections.sort(result);
		return result;
	}

	public void addReferencedForeignType(
			MetaType referencedForeignType) {
		if (!(referencedForeignType instanceof MetaDeclaredType)) {
			// it's inconvenient to check outside
			return;
		}
		MetaDeclaredType mdt = (MetaDeclaredType)referencedForeignType;
		String otherPackageName = mdt.getPackageName();
		if (!otherPackageName.equals(getPackageName()) && !otherPackageName.equals("java.lang")) {
			referencedForeignTypes.add(mdt);
		}
	}

	public void removeReferencedForeignType(
			MetaType referencedForeignType) {
		if (!(referencedForeignType instanceof MetaDeclaredType)) {
			// it's inconvenient to check outside
			return;
		}
		MetaDeclaredType mdt = (MetaDeclaredType)referencedForeignType;
		referencedForeignTypes.remove(mdt);
	}

	@Override
	public String getShortName() {
		return getSimpleName();
	}

	public void addMethod(MetaMethod mm) {
		methods.put(mm.getName(), mm);
	}
	
	public List<MetaMethod> getMethods() {
		List<MetaMethod> result = new ArrayList<MetaMethod>(methods.values());
		Collections.sort(result);
		return result;
	}
	
	public MetaMethod newMetaMethod(String methodName) {
		return new MetaMethod(this, methodName);
	}

	protected void analyzeMethods(Class<?> clazz) {
		logger.info("analyzing methods of "+getFqName());
		Method[] methods = clazz.getDeclaredMethods();
		for (Method mth : methods) {
			analyzeMethod(mth);
		}
	}

	private void analyzeMethod(Method mth) {
		String methodName = mth.getName();
		logger.fine("analyzing method " + methodName);
		MetaMethod mm = newMetaMethod(methodName);
		Type returnType = mth.getGenericReturnType();
		MetaType returnMetaType = MetaType.forType(returnType);
		mm.setReturnType(returnMetaType);
		mm.setModifiers(new MetaModifiers(mth.getModifiers()));
		Type[] parameterTypes = mth.getGenericParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			Type pti = parameterTypes[i];
			MetaType parameterMetaType = MetaType.forType(pti);
			MetaMethodParameter metaMethodParameter = new MetaMethodParameter("p"+(i+1), parameterMetaType);
			mm.addParameter(metaMethodParameter);
		}
		addMethod(mm);
	}

	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	public boolean isTarget() {
		return isTarget;
	}

	@Override
	public void setMetaAnnotationInstances(List<MetaAnnotationInstance> metaAnnotationInstances) {
		this.metaAnnotationInstances = metaAnnotationInstances;
	}

	@Override
	public void addMetaAnnotationInstance(MetaAnnotationInstance instance) {
		this.metaAnnotationInstances.add(instance);
	}
	
	@Override
	public List<MetaAnnotationInstance> getMetaAnnotationInstances() {
		return metaAnnotationInstances;
	}

	public void setMetaArgumentTypes(List<MetaType> metaArgumentTypes) {
		
		if (metaArgumentTypes.size() < 1) {
			// happens when we extend a parametrized type!
			return;
		}
		
		this.metaArgumentTypes = metaArgumentTypes;
		StringBuilder arguments = new StringBuilder("<");
		StringBuilder shortArguments = new StringBuilder("<");
		String sep = "";
		for (MetaType arg : metaArgumentTypes) {
			arguments.append(sep);
			shortArguments.append(sep);
			arguments.append(arg.getName());
			shortArguments.append(arg.getShortName());
			sep = ", ";
		}
		arguments.append(">");
		shortArguments.append(">");
		typeParameterString = arguments.toString();
		shortTypeParameterString = shortArguments.toString();
		//setFqName(createFqName(packageName, simpleName+typeParameterString));
	}

	public String getTypeParameterString() {
		return typeParameterString;
	}

	public void setTypeParameterString(String typeParameterString) {
		this.typeParameterString = typeParameterString;
	}

	public String getShortTypeParameterString() {
		return shortTypeParameterString;
	}

	public void setShortTypeParameterString(String shortTypeParameterString) {
		this.shortTypeParameterString = shortTypeParameterString;
	}

	public List<MetaType> getMetaArgumentTypes() {
		return metaArgumentTypes;
	}

	@Override
	public boolean isDeclaredType() {
		return true;
	}
}