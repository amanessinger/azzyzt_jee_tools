package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;


public abstract class GeneratorFeature extends Feature {
	
	private MetaModel model;
	protected Log logger;

	public GeneratorFeature(MetaModel model, Log logger) {
		this.model = model;
		this.logger = logger;
	}

	public abstract int generate(Parameters parameters);

	protected MetaModel getModel() {
		return model;
	}

}
