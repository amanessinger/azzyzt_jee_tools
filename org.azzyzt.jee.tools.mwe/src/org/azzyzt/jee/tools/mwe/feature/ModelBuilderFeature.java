package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.model.MetaModel;

public abstract class ModelBuilderFeature extends Feature {

	public ModelBuilderFeature() {
		super();
	}

	public abstract MetaModel build(Parameters parameters);

}
