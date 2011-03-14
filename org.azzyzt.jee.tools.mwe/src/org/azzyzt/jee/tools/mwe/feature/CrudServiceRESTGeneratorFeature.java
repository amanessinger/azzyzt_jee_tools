package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.builder.CrudServiceRESTFullModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRESTRestrictedModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTExceptionMapperModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTInterceptorModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.RESTServletModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;

public class CrudServiceRESTGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER = "Source Folder";

	public CrudServiceRESTGeneratorFeature(MetaModel entityModel, Log logger) {
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
		
		MetaModel targetModel = new CrudServiceRESTFullModelBuilder(getModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTFullGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated = targetGen.generate();
		
		targetModel = new CrudServiceRESTRestrictedModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTRestrictedGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new RESTServletModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new RESTExceptionMapperModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaGroup", logger);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new RESTInterceptorModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaRESTInterceptorGroup", logger);
		targetGen.setGenerateFields(false);
		targetGen.setGenerateDefaultConstructor(false);
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		return numberOfSourcesGenerated;
	}

}
