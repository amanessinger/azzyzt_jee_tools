package org.azzyzt.jee.tools.mwe.builder;

import java.util.List;

import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class EntityModelBuilder {

	private TargetEnumerator enumerator;

    public EntityModelBuilder() {
        enumerator = new EntityEnumerator(EntityEnumerator.PERSISTENCE_UNIT_WILDCARD);
    }

    public EntityModelBuilder(String persistenceUnitName) {
        enumerator = new EntityEnumerator(persistenceUnitName);
    }

    public MetaModel build() {
        MetaModel entityModel = new MetaModel();
        entityModel.excludeMethodsFromModel();
        entityModel.excludeStaticFieldsFromModel();
        for (String targetPackage : enumerator.getTargetPackageNames()) {
        	entityModel.follow(targetPackage);
        }
        List<String> entityNames = enumerator.getFullyQualifiedTargetNames();
        entityModel.build(entityNames);        
        return entityModel;
    }

}
