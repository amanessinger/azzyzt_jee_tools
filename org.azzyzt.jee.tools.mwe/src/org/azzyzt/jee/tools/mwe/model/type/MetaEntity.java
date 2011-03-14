package org.azzyzt.jee.tools.mwe.model.type;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.azzyzt.jee.tools.mwe.model.association.MetaAssociationEndpoint;

public class MetaEntity extends MetaClass {

	private List<MetaAssociationEndpoint> associationEndpoints;
	private MetaField idField;
	private MetaEntityField createUserField;
	private boolean isUsingCreateUserField = false;
	private MetaEntityField createTimestampField;
	private boolean isUsingCreateTimestampField = false;
	private MetaEntityField modificationUserField;
	private boolean isUsingModificationUserField = false;
	private MetaEntityField modificationTimestampField;
	private boolean isUsingModificationTimestampField = false;
	private boolean isCombinedId = false;
	private MetaType combinedIdType;

	public static boolean isEntity(Class<?> clazz) {
		Entity entityAnnotation = clazz.getAnnotation(Entity.class);
		if (entityAnnotation != null) {
			return true;
		}
		return false;
	}
	
	public static MetaEntity forName(String packageName, String simpleName) {
		MetaEntity result = getOrConstruct(null, packageName, simpleName);
		return result;
	}
	
	public static MetaEntity forType(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String simpleName = clazz.getSimpleName();
		MetaEntity result = getOrConstruct(clazz, packageName, simpleName);
		return result;
	}

	protected static MetaEntity getOrConstruct(Class<?> clazz,
			String packageName, String simpleName) {
		MetaEntity result = (MetaEntity)MetaTypeRegistry.metaTypeForName(createFqName(packageName, simpleName));
		if (result == null || !(result instanceof MetaEntity)) {
			result = new MetaEntity(clazz, packageName, simpleName);
			result.postConstructionAnalysis();
		}
		return result;
	}
	
	@Override
	public void postConstructionAnalysis() {
		super.postConstructionAnalysis();
	}

	protected MetaEntity(Class<?> clazz, String packageName, String simpleName) {
		super(clazz, packageName, simpleName);
	}

	@Override
	public MetaField newMetaField(String fieldName) {
		return new MetaEntityField(this, fieldName);
	}
	
	public MetaField getIdField() {
		return idField;
	}

	public void setIdField(MetaField mf) {
		this.idField = mf;
	}
	
	public void addAssociationEndpoint(MetaAssociationEndpoint mae) {
		if (!this.equals(mae.getSourceField().getOwnerMdt())) {
			throw new ToolError("Can't add foreign association endpoint");
		}
		getAssociationEndpoints().add(mae);
	}

	public List<MetaAssociationEndpoint> getAssociationEndpoints() {
		if (associationEndpoints == null) {
			 associationEndpoints = new ArrayList<MetaAssociationEndpoint>();
		}
		return associationEndpoints;
	}

	@Override
	public boolean isEntity() {
		return true;
	}

	public boolean isCombinedId() {
		return isCombinedId;
	}

	public void setCombinedId(boolean isCombinedId) {
		this.isCombinedId = isCombinedId;
	}

	public MetaType getCombinedIdType() {
		return combinedIdType;
	}

	public void setCombinedIdType(MetaType combinedIdType) {
		this.combinedIdType = combinedIdType;
	}

	/*
	 * Create user
	 */
	
	public void setCreateUserField(MetaEntityField createUserField) {
		this.createUserField = createUserField;
		if (createUserField != null) {
			this.isUsingCreateUserField = true;
		} else {
			this.isUsingCreateUserField = false;
		}
	}

	public MetaEntityField getCreateUserField() {
		return createUserField;
	}

	public void setUsingCreateUserField(boolean isUsingCreateUserField) {
		// not strictly needed, but we may decide to serialize models at one time
		this.isUsingCreateUserField = isUsingCreateUserField;
	}

	public boolean isUsingCreateUserField() {
		return isUsingCreateUserField;
	}

	/*
	 * Create timestamp
	 */
	
	public void setCreateTimestampField(MetaEntityField createTimestampField) {
		this.createTimestampField = createTimestampField;
		if (createTimestampField != null) {
			this.isUsingCreateTimestampField = true;
		} else {
			this.isUsingCreateTimestampField = false;
		}
	}

	public MetaEntityField getCreateTimestampField() {
		return createTimestampField;
	}

	public void setUsingCreateTimestampField(boolean isUsingCreateTimestampField) {
		// not strictly needed, but we may decide to serialize models at one time
		this.isUsingCreateTimestampField = isUsingCreateTimestampField;
	}

	public boolean isUsingCreateTimestampField() {
		return isUsingCreateTimestampField;
	}

	/*
	 * Modification user
	 */
	
	public void setModificationUserField(MetaEntityField modificationUserField) {
		this.modificationUserField = modificationUserField;
		if (modificationUserField != null) {
			this.isUsingModificationUserField = true;
		} else {
			this.isUsingModificationUserField = false;
		}
	}

	public MetaEntityField getModificationUserField() {
		return modificationUserField;
	}

	public void setUsingModificationUserField(boolean isUsingModificationUserField) {
		// not strictly needed, but we may decide to serialize models at one time
		this.isUsingModificationUserField = isUsingModificationUserField;
	}

	public boolean isUsingModificationUserField() {
		return isUsingModificationUserField;
	}

	/*
	 * Modification timestamp
	 */
	
	public void setModificationTimestampField(MetaEntityField modificationTimestampField) {
		this.modificationTimestampField = modificationTimestampField;
		if (modificationTimestampField != null) {
			this.isUsingModificationTimestampField = true;
		} else {
			this.isUsingModificationTimestampField = false;
		}
	}

	public MetaEntityField getModificationTimestampField() {
		return modificationTimestampField;
	}

	public void setUsingModificationTimestampField(boolean isUsingModificationTimestampField) {
		// not strictly needed, but we may decide to serialize models at one time
		this.isUsingModificationTimestampField = isUsingModificationTimestampField;
	}

	public boolean isUsingModificationTimestampField() {
		return isUsingModificationTimestampField;
	}

}
