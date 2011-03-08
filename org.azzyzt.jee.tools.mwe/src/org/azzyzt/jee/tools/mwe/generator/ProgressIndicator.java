package org.azzyzt.jee.tools.mwe.generator;

import java.util.logging.Logger;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class ProgressIndicator {
	
	// TODO make ProgressIndicator an interface and pass it into a feature via a parameter

	private static Logger logger = Logger.getLogger(ProgressIndicator.class.getPackage().getName());
	
    private int units;
    private int currentUnit = 0;

    public ProgressIndicator(MetaModel model) {
        this.units = model.getTargetMetaDeclaredTypes().size();
    }

    public void advance(MetaType mc) {
        currentUnit++;
        logger.info("Generating artifacts for class "+mc.getId()+" ("+currentUnit+"/"+units+")");
    }
}
