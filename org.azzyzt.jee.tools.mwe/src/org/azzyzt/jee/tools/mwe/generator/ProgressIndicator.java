package org.azzyzt.jee.tools.mwe.generator;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class ProgressIndicator {
	
	// TODO make ProgressIndicator an interface and pass it into a feature via a parameter

    private int currentUnit = 0;

    public ProgressIndicator(MetaModel model) {
    }

    public void advance(MetaType mc) {
        currentUnit++;
    }
}
