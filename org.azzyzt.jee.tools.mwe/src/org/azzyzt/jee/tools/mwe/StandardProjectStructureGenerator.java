/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.tools.mwe;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.feature.CrudServiceBeansGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.CrudServiceRESTGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.DtoGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityDtoConverterGeneratorFeature;
import org.azzyzt.jee.tools.mwe.feature.EntityModelBuilderFeature;
import org.azzyzt.jee.tools.mwe.feature.Parameters;
import org.azzyzt.jee.tools.mwe.feature.SingleTargetsGeneratorFeature;
import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

public class StandardProjectStructureGenerator {

	public static void main(String[] args) {
        doWork(args, new StreamLog());
    }

	public static void sideEntrance(
			String[] args, 
			ConcurrentLinkedQueue<String> log) 
	{
		doWork(args, new QueueLog(log));
	}

	protected static void doWork(String[] args, Log logger) {
		if (args.length < 1) {
			logger.log("usage: StandardProjectStructureGenerator <project_prefix> [<persistence_unit_name>]");
            return;
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

        SingleTargetsGeneratorFeature singleTargetsGen = new SingleTargetsGeneratorFeature(entityModel, logger);
        parameters = singleTargetsGen.getParameters();
        parameters.byName(SingleTargetsGeneratorFeature.SOURCE_FOLDER).setValue(ejbSourceFolder);
		numberOfSourcesGenerated = singleTargetsGen.generate(parameters);
		logger.log(numberOfSourcesGenerated+" eao files generated");
		
		DtoGeneratorFeature dtoGen = new DtoGeneratorFeature(entityModel, logger);
        parameters = dtoGen.getParameters();
        parameters.byName(DtoGeneratorFeature.SOURCE_FOLDER).setValue(ejbClientSourceFolder);
        numberOfSourcesGenerated = dtoGen.generate(parameters);
        logger.log(numberOfSourcesGenerated+" dto files generated");
        
        EntityDtoConverterGeneratorFeature convGen = new EntityDtoConverterGeneratorFeature(entityModel, logger);
        parameters = convGen.getParameters();
        parameters.byName(EntityDtoConverterGeneratorFeature.SOURCE_FOLDER).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = convGen.generate(parameters);
		logger.log(numberOfSourcesGenerated+" converter files generated");
		
		CrudServiceBeansGeneratorFeature svcGen = new CrudServiceBeansGeneratorFeature(entityModel, logger);
		parameters = svcGen.getParameters();
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_CLIENT_PROJECT).setValue(ejbClientSourceFolder);
        parameters.byName(CrudServiceBeansGeneratorFeature.SOURCE_FOLDER_EJB_PROJECT).setValue(ejbSourceFolder);
        numberOfSourcesGenerated = svcGen.generate(parameters);
        logger.log(numberOfSourcesGenerated+" service beans and service interfaces generated");
		
		CrudServiceRESTGeneratorFeature restGen = new CrudServiceRESTGeneratorFeature(entityModel, logger);
		parameters = restGen.getParameters();
        parameters.byName(CrudServiceRESTGeneratorFeature.SOURCE_FOLDER).setValue(restSourceFolder);
        numberOfSourcesGenerated = restGen.generate(parameters);
        logger.log(numberOfSourcesGenerated+" REST wrapper files generated");
	}

}
