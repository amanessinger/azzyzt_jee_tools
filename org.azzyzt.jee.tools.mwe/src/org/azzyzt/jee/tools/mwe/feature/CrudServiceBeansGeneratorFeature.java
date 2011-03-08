package org.azzyzt.jee.tools.mwe.feature;

import org.azzyzt.jee.tools.mwe.builder.CrudServiceFullBeansModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceFullInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRestrictedBeansModelBuilder;
import org.azzyzt.jee.tools.mwe.builder.CrudServiceRestrictedInterfaceModelBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class CrudServiceBeansGeneratorFeature extends GeneratorFeature {

	public static final String SOURCE_FOLDER_EJB_PROJECT = "Source Folder (EJB Project)";
	public static final String SOURCE_FOLDER_CLIENT_PROJECT = "Source Folder (Client Project)";

	public CrudServiceBeansGeneratorFeature(MetaModel entityModel) {
		super(entityModel);
	}

	@Override
	public Parameters getParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(SOURCE_FOLDER_EJB_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		parameters.add(new Parameter(SOURCE_FOLDER_CLIENT_PROJECT, ParameterType.SourceFolder, Parameter.IS_MANDATORY));
		return parameters;
	}
	
	@Override
	public int generate(Parameters parameters) {
		int numberOfSourcesGenerated;
		String sourceFolder;
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_CLIENT_PROJECT).getValue();
		
		MetaModel targetModel = new CrudServiceFullInterfaceModelBuilder(getModel(), null).build();
		JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceFullGroup");
		numberOfSourcesGenerated = targetGen.generate();
		
		targetModel = new CrudServiceRestrictedInterfaceModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceRestrictedGroup");
		numberOfSourcesGenerated += targetGen.generate();
		
		sourceFolder = (String)parameters.byName(SOURCE_FOLDER_EJB_PROJECT).getValue();
		
		targetModel = new CrudServiceFullBeansModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceFullGroup");
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		targetModel = new CrudServiceRestrictedBeansModelBuilder(getModel(), null).build();
		targetGen = new JavaGenerator(targetModel, sourceFolder, "javaCrudServiceRestrictedGroup");
		targetGen.setGenerateGettersSetters(false);
		numberOfSourcesGenerated += targetGen.generate();
		
		return numberOfSourcesGenerated;
	}

}
