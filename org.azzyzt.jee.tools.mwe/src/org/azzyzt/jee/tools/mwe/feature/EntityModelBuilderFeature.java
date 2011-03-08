package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.builder.EntityModelBuilder;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class EntityModelBuilderFeature extends ModelBuilderFeature {
	
	public static final String PERSISTENCE_UNIT_NAME = "Persistence Unit Name";

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(PERSISTENCE_UNIT_NAME, ParameterType.String, Parameter.IS_OPTIONAL));
		return parameters;
	}
	
	@Override
	public MetaModel build(Parameters parameters) {
		String persistenceUnitName = null;
		
		persistenceUnitName = (String)parameters.byName(PERSISTENCE_UNIT_NAME).getValue();

        EntityModelBuilder emb;
        if (persistenceUnitName != null) {
            emb = new EntityModelBuilder(persistenceUnitName);
        } else {
            emb = new EntityModelBuilder();
        }
		MetaModel entityModel = emb.build();
		return entityModel;
	}

}
