package org.azzyzt.jee.tools.mwe.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.azzyzt.jee.runtime.meta.AzzyztGeneratorCutback;
import org.azzyzt.jee.runtime.meta.AzzyztGeneratorOption;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaStandardDefs;
import org.azzyzt.jee.tools.mwe.model.type.MetaType;

public class GeneratorOptions {

	private Set<AzzyztGeneratorCutback> cutbacks = new HashSet<AzzyztGeneratorCutback>();
	private Set<AzzyztGeneratorOption>  options = new HashSet<AzzyztGeneratorOption>();

	public static GeneratorOptions analyzeAzzyztant(MetaClass azzyztant) {
		GeneratorOptions result = new GeneratorOptions();
		MetaStandardDefs std = MetaType.getStandardtypes();
		List<MetaAnnotationInstance> mais = azzyztant.getMetaAnnotationInstances();
		for (MetaAnnotationInstance mai : mais) {
			if (mai.getMetaAnnotation().equals(std.azzyztGeneratorOptions)) {
				
				AzzyztGeneratorCutback[] cutbacks = 
						(AzzyztGeneratorCutback[])mai.getRawValue("cutbacks");
				if (cutbacks != null) {
					for (AzzyztGeneratorCutback c : cutbacks) {
						result.addCutback(c);
					}
				}
				
				AzzyztGeneratorOption[] options = 
						(AzzyztGeneratorOption[])mai.getRawValue("options");
				if (options != null) {
					for (AzzyztGeneratorOption o : options) {
						result.addOptionk(o);
					}
				}
				
			}
		}
		return result;
	}
	
	public boolean hasCutback(AzzyztGeneratorCutback c) {
		return cutbacks.contains(c);
	}

	public boolean addCutback(AzzyztGeneratorCutback c) {
		return cutbacks.add(c);
	}

	public boolean hasOption(AzzyztGeneratorOption o) {
		return options.contains(o);
	}
	
	public void addOptionk(AzzyztGeneratorOption o) {
		options.add(o);
	}
	
}
