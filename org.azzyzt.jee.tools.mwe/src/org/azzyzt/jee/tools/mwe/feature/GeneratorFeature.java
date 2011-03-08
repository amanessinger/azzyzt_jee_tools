package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.model.MetaModel;


public abstract class GeneratorFeature extends Feature {
	
	private MetaModel model;

	public GeneratorFeature(MetaModel model) {
		this.model = model;
	}

	public abstract int generate(Parameters parameters);

	protected MetaModel getModel() {
		return model;
	}

}
