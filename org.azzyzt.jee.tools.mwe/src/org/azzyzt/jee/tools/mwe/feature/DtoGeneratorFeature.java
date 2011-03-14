package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.builder.DtoModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public class DtoGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER = "Source Folder";

	public DtoGeneratorFeature(MetaModel entityModel, Log logger) {
		super(entityModel, logger);
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(SOURCE_FOLDER, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		return parameters;
	}
	
	@Override
	public int generate(Parameters parameters) {
		int numberOfSourcesGenerated;
		String sourceFolder;
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER).getValue();
		
		MetaModel targetModel = new DtoModelBuilder(getModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", logger);
		numberOfSourcesGenerated = targetGen.generate();
		
		return numberOfSourcesGenerated;
	}

}
