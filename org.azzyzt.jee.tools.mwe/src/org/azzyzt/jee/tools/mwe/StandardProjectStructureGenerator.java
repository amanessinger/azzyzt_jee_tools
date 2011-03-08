package org.azzyzt.jee.tools.mwe;

import java.util.logging.Logger;

import org.azzyzt.jee.tools.mwe.feature.CrudServiceBeansGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.CrudServiceRESTGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.DtoGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityDtoConverterGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityModelBuilderFeature;
import org.azzyzt.jee.tools.mwe.feature.SingleTargetsGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.Parameters;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class StandardProjectStructureGenerator {

	public static Logger logger = Logger.getLogger(StandardProjectStructureGenerator.class.getPackage().getName());

	public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("usage: StandardProjectStructureGenerator <project_prefix> [<persistence_unit_name>]");
            System.exit(1);
        }

        String projectPrefix = args[0];
        String ejbSourceFolder = projectPrefix+"EJB/generated";
        String ejbClientSourceFolder = projectPrefix+"EJBClient/generated";
        String restSourceFolder = projectPrefix+"Servlets/generated";

        Parameters parameters;
        int numberOfSourcesGenerated;
        
        EntityModelBuilderFeature embf = new EntityModelBuilderFeature();
        
        // TODO id fields of entities currently must be called id (EntityBase!!!) 
        
		parameters = embf.getParameters();
        if (2 == args.length) {
            parameters.byName(EntityModelBuilderFeature.PERSISTENCE_UNIT_NAME).setValue(args[1]);
        }
        MetaModel entityModel = embf.build(parameters);
		
        SingleTargetsGeneratorFeature eaoGen = new SingleTargetsGeneratorFeature(entityModel);
        parameters = eaoGen.getParameters();
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER).setValue(ejbSourceFolder);
		numberOfSourcesGenerated = eaoGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" eao files generated");
		
		DtoGeneratorFeature dtoGen = new DtoGeneratorFeature(entityModel);
        parameters = dtoGen.getParameters();
        parameters.byName(DtoGeneratorFeature.SOURCE_FOLDER).setValue(ejbClientSourceFolder);
        numberOfSourcesGenerated = dtoGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" dto files generated");
        
        EntityDtoConverterGeneratorFeature convGen = new EntityDtoConverterGeneratorFeature(entityModel);
        parameters = convGen.getParameters();
        parameters.byName(EntityDtoConverterGeneratorFeature.SOURCE_FOLDER).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = convGen.generate(parameters);
		logger.info(numberOfSourcesGenerated+" converter files generated");
		
		CrudServiceBeansGeneratorFeature svcGen = new CrudServiceBeansGeneratorFeature(entityModel);
		parameters = svcGen.getParameters();
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbClientSourceFolder);
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = svcGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" service beans and service interfaces generated");
		
		CrudServiceRESTGeneratorFeature restGen = new CrudServiceRESTGeneratorFeature(entityModel);
		parameters = restGen.getParameters();
        parameters.byName(CrudServiceRESTGeneratorFeature.SOURCE_FOLDER).setValue(restSourceFolder);
        numberOfSourcesGenerated = restGen.generate(parameters);
        logger.info(numberOfSourcesGenerated+" REST wrapper");
    }

}
