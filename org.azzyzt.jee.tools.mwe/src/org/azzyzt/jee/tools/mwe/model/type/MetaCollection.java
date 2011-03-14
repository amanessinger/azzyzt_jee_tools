package org.azzyzt.jee.tools.mwe.model.type;

import java.util.List;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class MetaCollection extends MetaAssemblage {
	
	private String fqName;
	private String packageName;
	private String simpleName;
	private String typeParameters;
	private String shortTypeParameters;
	private MetaDeclaredType metaRawType;

	public static MetaCollection forNameAndMetaTypes(
			String packageName, String simpleName,
			MetaDeclaredType metaRawtype, List<MetaType> argumentTypes
	) {
		StringBuilder arguments = new StringBuilder("<");
		StringBuilder shortArguments = new StringBuilder("<");
		String sep = "";
		for (MetaType arg : argumentTypes) {
			arguments.append(sep);
			shortArguments.append(sep);
			arguments.append(arg.getName());
			shortArguments.append(arg.getShortName());
			sep = ", ";
		}
		arguments.append(">");
		shortArguments.append(">");
		String typeParameters = arguments.toString();
		String shortTypeParameters = shortArguments.toString();
		String theFqName = MetaDeclaredType.createFqName(packageName, simpleName+typeParameters);
		MetaCollection result = (MetaCollection)MetaTypeRegistry.metaTypeForName(theFqName);
		if (result == null || !(result instanceof MetaCollection)) {
			result = new MetaCollection(packageName, simpleName, metaRawtype, typeParameters, shortTypeParameters, argumentTypes);
		}
		return result;
	}
	
	protected MetaCollection(
			String packageName, String simpleName, MetaDeclaredType metaRawtype, String typeParameters, 
			String shortTypeParameters, List<MetaType> argumentTypes
	) {
		super(MetaDeclaredType.createFqName(packageName, simpleName+typeParameters));
		setPackageName(packageName);
		setSimpleName(simpleName);
		setFqName(MetaDeclaredType.createFqName(packageName, simpleName));
		setMetaRawType(metaRawtype);
		setTypeParameters(typeParameters);
		setShortTypeParameters(shortTypeParameters);
		for (MetaType arg : argumentTypes) {
			addMemberType(arg);
		}
	}
	
	@Override
	public boolean isAcceptableMemberType(MetaType mt) {
		// These are variants of java.util.Collection
		if (mt instanceof MetaBuiltin) {
			return false;
		}
		return true;
	}

	@Override
	public int maximumNumberOfMemberTypes() {
		/*
		 *  Maps may contain two member types. Of course it would be possible 
		 *  to define collections with three or more member types, but it will
		 *  be fine to close off at two  
		 */
		return 2;
	}

	public String getFqName() {
		return fqName;
	}

	protected void setFqName(String fqName) {
		this.fqName = fqName;
	}

	public String getPackageName() {
		return packageName;
	}

	protected void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	protected void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	@Override
	public String getShortName() {
		return getSimpleName()+getShortTypeParameters();
	}

	public String getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(String typeParameters) {
		this.typeParameters = typeParameters;
	}

	public String getShortTypeParameters() {
		return shortTypeParameters;
	}

	public void setShortTypeParameters(String shortTypeParameters) {
		this.shortTypeParameters = shortTypeParameters;
	}

	public MetaDeclaredType getMetaRawType() {
		return metaRawType;
	}

	public void setMetaRawType(MetaDeclaredType metaRawType) {
		this.metaRawType = metaRawType;
	}

	public MetaEntity getEntityMemberType() {
		MetaEntity result;
		if (getMemberTypes().size() == 1) {
			// this should cover lists and sets
			result = (MetaEntity)getMemberTypes().get(0);
		} else {
			// Associations mapped as maps should be <idtype,entitytype>
			result = (MetaEntity)getMemberTypes().get(1);
		}
		return result;
	}

	private enum CollectionType { List, Set, Map }
	
	private CollectionType getCollectionType() {
		if (metaRawType == null || metaRawType.getClazz() == null) {
			throw new ToolError("Can't determine collection type without a class instance for the raw type");
		}
		Class<?> clazz = metaRawType.getClazz();
		if (java.util.List.class.isAssignableFrom(clazz)) return CollectionType.List;
		if (java.util.Set.class.isAssignableFrom(clazz)) return CollectionType.Set;
		if (java.util.Map.class.isAssignableFrom(clazz)) return CollectionType.Map;
		throw new ToolError("Funny, "+clazz.getCanonicalName()+" seems to be an unsupported collection");
	}
	
	public boolean isList() {
		return getCollectionType().equals(CollectionType.List);
	}
	
	public boolean isSet() {
		return getCollectionType().equals(CollectionType.Set);
	}
	
	public boolean isMap() {
		return getCollectionType().equals(CollectionType.Map);
	}

	@Override
	public boolean isEntityCollection() {
		MetaType candidate;
		if (getMemberTypes().size() == 1) {
			// this should cover lists and sets
			candidate = getMemberTypes().get(0);
		} else {
			// Associations mapped as maps should be <idtype,entitytype>
			candidate = getMemberTypes().get(1);
		}
		return candidate.isEntity();
	}
	
}
