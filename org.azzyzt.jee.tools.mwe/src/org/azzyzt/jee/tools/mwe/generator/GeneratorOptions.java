package org.azzyzt.jee.tools.mwe.generator;

import java.util.HashSet;
import java.util.Set;

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;

public class GeneratorOptions {

	private Set<AzzyztGeneratorCutback> cutbacks = new HashSet<AzzyztGeneratorCutback>();

	public boolean hasCutback(AzzyztGeneratorCutback c) {
		return cutbacks.contains(c);
	}

	public boolean addCutback(AzzyztGeneratorCutback c) {
		return cutbacks.add(c);
	}
	
}
