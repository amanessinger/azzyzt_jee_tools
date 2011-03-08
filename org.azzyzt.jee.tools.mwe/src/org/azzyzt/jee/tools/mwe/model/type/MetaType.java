package org.azzyzt.jee.tools.mwe.model.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotation;

public abstract class MetaType {

	public static Logger logger = Logger.getLogger(MetaType.class.getPackage().getName());

	private static final MetaStandardDefs standardTypes = new MetaStandardDefs();
	
	private MetaTypeId id;
	private String name;
	private Properties properties = new Properties(); // may be set by a synthesizing builder

	protected MetaType superMetaClass = null;
	
	private Set<MetaType> subtypes = new HashSet<MetaType>();

	public static MetaType forType(Type type) {
		logger.fine("getting MetaType for type "+type);
		
		MetaType result = null;
		
		// types come in various guises, thus we instantiate them later
		boolean isBuiltin = false;
		boolean isClass = false;
		boolean isEntity = false;
		boolean isAnnotation = false;
		boolean isInterface = false;
		boolean isArray = false;
		boolean isEnum = false;
		
		String packageName = null;
		String simpleName = null;

		Class<?> rawTypeClass = null;
		List<MetaType> metaArgumentTypes = new ArrayList<MetaType>();
		
		MetaType arrayComponentMetaType = null;
		
		logger.finest("type = " + type);
		
		if (type instanceof GenericArrayType) {
			// this is a special kind of array holding parametrized types like List<String> or Class<?>
			logger.finest(type+" is GenericArrayType");
			isArray = true;
			GenericArrayType gat = (GenericArrayType)type;
			Type componentType = gat.getGenericComponentType();
			arrayComponentMetaType = forType(componentType);
			simpleName = type.toString();
		} else if (type instanceof TypeVariable<?>) {
			logger.finest(type+" is TypeVariable<?>");
			return MetaTypeVariable.forName(type.toString());
		} else if (type instanceof Class<?>) {
			logger.finest(type+" is Class<?>");
			Class<?> clazz = (Class<?>)type;
			Package itsPackage = clazz.getPackage();
			simpleName = clazz.getSimpleName();
			if (itsPackage != null) {
				packageName = itsPackage.getName();
			}
			Class<?> componentType = clazz.getComponentType();
			if (componentType != null) {
				isArray = true;
				arrayComponentMetaType = forType(componentType);
			} else if (clazz.isAnnotation()) {
				isAnnotation = true;
			} else if (clazz.isInterface()) {
				isInterface = true;
			} else if (MetaEntity.isEntity(clazz)) {
				logger.finest(clazz+" is an entity class");
				isEntity = true;
			} else if (packageName == null && !isArray) {
				logger.finest(clazz+" is a builtin");
				isBuiltin = true;
			} else if (clazz.isEnum()) {
				logger.finest(clazz+" is an enum");
				isEnum = true;
			} else {
				logger.finest(type+" is normal class "
						+packageName+"."+simpleName);
				isClass = true;
			}
		} else if (type instanceof WildcardType) {
			logger.finest(type+" is WildcardType");
			WildcardType wct = (WildcardType)type;
			simpleName = wct.toString();
			MetaWildcard metaWildcard = new MetaWildcard(simpleName);
			for (Type ub : wct.getUpperBounds()) {
				logger.finest("    "+ub+" is upper bound");
				MetaType metaUb = forType(ub);
				metaWildcard.addUpperBound(metaUb);
			}
			for (Type lb : wct.getLowerBounds()) {
				logger.finest("    "+lb+" is lower bound");
				MetaType metaLb = forType(lb);
				metaWildcard.addLowerBound(metaLb);
			}
			return metaWildcard;
		} else if (type instanceof ParameterizedType) {
			logger.finest(type+" is ParameterizedType");
			ParameterizedType pt = (ParameterizedType) type;
			Type rawType = pt.getRawType();
			rawTypeClass = (Class<?>)rawType;
			Package itsPackage = rawTypeClass.getPackage();
			simpleName = rawTypeClass.getSimpleName();
			if (itsPackage != null) {
				packageName = itsPackage.getName();
			}
			logger.finest("raw type: " + rawType);
			logger.finest("owner type: " + pt.getOwnerType());
			logger.finest("actual type args:");
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			
			// special-case Class<?>
			if (rawType.equals(java.lang.Class.class)) {
				logger.finest(type+" is Class<?>");
				if (actualTypeArguments.length > 1) {
					throw new ToolError(type+" should be Class<?> and can't have more than one type argument, can it?");
				}
				Type t = actualTypeArguments[0];
				logger.finest("    " + t);
				MetaType metaArgumentType = forType(t);
				MetaClassClass metaClassClass = MetaClassClass.forMetaType(metaArgumentType);
				return metaClassClass;
			}

			// get type parameters
			for (Type t : actualTypeArguments) {
				logger.finest("    " + t);
				MetaType metaArgumentType = forType(t);
				metaArgumentTypes.add(metaArgumentType);
			}

			// special-case collections 
			if (false 
					|| java.util.Collection.class.isAssignableFrom(rawTypeClass) 
					|| java.util.Map.class.isAssignableFrom(rawTypeClass)
			) {
				logger.finest(type+" is Collection");
				MetaDeclaredType metaRawtype = (MetaDeclaredType)forType(rawTypeClass);
				MetaCollection metaCollection = MetaCollection.forNameAndMetaTypes(
						packageName, simpleName, metaRawtype, metaArgumentTypes
				);
				return metaCollection;
			}
			
			// do the parametrized versions of MDTs
			if (rawTypeClass.isAnnotation()) {
				throw new ToolError("This can't happen: an annotation cannot be parametrized");
			} else if (rawTypeClass.isInterface()) {
				logger.finest(type+" is a parametrized interface");
				isInterface = true;
			} else if (MetaEntity.isEntity(rawTypeClass)) {
				logger.finest(type+" is a parametrized entity class");
				isEntity = true;
			} else {
				logger.finest(type+" is a parametrized normal class");
				isClass = true;
			}
		}
		
		if (isBuiltin) {
			MetaBuiltin metaBuiltin = MetaBuiltin.forName(simpleName);
			return metaBuiltin;
		} else if (isEntity) {
			Class<?> typeToUse = (rawTypeClass != null ? rawTypeClass : (Class<?>)type);
			logger.info("entity typeToUse = "+typeToUse);
			MetaDeclaredType metaEntity = MetaEntity.getOrConstruct(typeToUse, packageName, simpleName);
			metaEntity.setMetaArgumentTypes(metaArgumentTypes);
			return metaEntity;
		} else if (isEnum) {
			MetaDeclaredType metaEnum = MetaEnum.getOrConstruct((Class<?>)type, packageName, simpleName);
			return metaEnum;
		} else if (isClass) {
			Class<?> typeToUse = (rawTypeClass != null ? rawTypeClass : (Class<?>)type);
			MetaDeclaredType metaClass = MetaClass.getOrConstruct(typeToUse, packageName, simpleName);
			metaClass.setMetaArgumentTypes(metaArgumentTypes);
			return metaClass;
		} else if (isAnnotation) {
			Class<?> typeToUse = (rawTypeClass != null ? rawTypeClass : (Class<?>)type);
			MetaAnnotation metaAnnotation = MetaAnnotation.getOrConstruct(typeToUse, packageName, simpleName);
			metaAnnotation.setMetaArgumentTypes(metaArgumentTypes);
			return metaAnnotation;
		} else if (isInterface) {
			Class<?> typeToUse = (rawTypeClass != null ? rawTypeClass : (Class<?>)type);
			MetaInterface metaInterface = MetaInterface.getOrConstruct(typeToUse, packageName, simpleName);
			metaInterface.setMetaArgumentTypes(metaArgumentTypes);
			return metaInterface;
		} else if (isArray) {
			MetaArray metaArray = MetaArray.forName(arrayComponentMetaType.getName()+"[]");
			metaArray.addMemberType(arrayComponentMetaType);
			return metaArray;
		}
		
		if (result == null) {
			logger.warning("MetaType for type "+type+" not yet implemented");
		}
		return result;
	}
	
	protected MetaType(String name) {
		setId(new MetaTypeId(name));
		setName(name);
	}
	
	public MetaTypeId getId() {
		return id;
	}

	protected void setId(MetaTypeId id) {
		this.id = id;
		MetaTypeRegistry.add(this);
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaType other = (MetaType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" [id=" + id + "]";
	}

	public boolean getCanBeNull() {
		return !(this instanceof MetaBuiltin);
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

	public MetaType getSuperMetaClass() {
		return superMetaClass;
	}

	protected void addSubtype(MetaType subType) {
		subtypes.add(subType);
		MetaType subsSuper = subType.getSuperMetaClass();
		if (subsSuper == null || !subsSuper.equals(this)) {
			subType.setSuperMetaClass(this);
		}
	}

	public void setSuperMetaClass(MetaType superMetaClass) {
		this.superMetaClass = superMetaClass;
		if (this instanceof MetaDeclaredType) {
			MetaDeclaredType mdt = (MetaDeclaredType)this;
			mdt.addReferencedForeignType(superMetaClass);
			if (!superMetaClass.getSubtypes().contains(mdt)) {
				superMetaClass.addSubtype(mdt);
			}
		}
	}

	public Set<MetaType> getSubtypes() {
		return new HashSet<MetaType>(subtypes);
	}

	public boolean isImplicitlyImported() {
		return false;
	}

	public boolean isBuiltinType() {
		return false;
	}

	public boolean isDeclaredType() {
		return false;
	}

	public boolean isEntity() {
		return false;
	}

	public boolean isEntityCollection() {
		return false;
	}

	public static MetaStandardDefs getStandardtypes() {
		return standardTypes;
	}
}