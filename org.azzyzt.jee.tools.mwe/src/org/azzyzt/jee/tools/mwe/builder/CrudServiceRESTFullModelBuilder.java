package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.annotation.MetaAnnotationInstance;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;

public class CrudServiceRESTFullModelBuilder extends DerivedModelBuilder implements Builder {

	public CrudServiceRESTFullModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		for (MetaEntity me : masterModel.getTargetEntities()) {
			MetaClass dto = (MetaClass) me.getProperty("dto");
			MetaClass svcBean = (MetaClass) me.getProperty("svcFullBean");
			MetaClass restInterceptor = (MetaClass) masterModel.getProperty("rest_interceptor");

			// create MetaClass
			String packageName = derivePackageNameFromEntity(me, "service");
			String simpleName = me.getSimpleName();
			String pathString = simpleName.toLowerCase();
			simpleName += "FullDelegator";
			MetaClass target = MetaClass.forName(packageName, simpleName);
			me.setProperty("RESTFullDelegator", target);
			target.setModifiers(std.mod_public);
			target.setSuperMetaClass(std.restDelegatorBase);
			target.addMetaAnnotationInstance(new MetaAnnotationInstance(std.javaxEjbStateless, target));
			MetaAnnotationInstance path = new MetaAnnotationInstance(std.javaxWsRsPath, target);
			path.setElement("value", pathString);
			target.addMetaAnnotationInstance(path);
			
			target.addReferencedForeignType(dto);
			target.addReferencedForeignType(restInterceptor);
			target.addReferencedForeignType(std.javaxInterceptorInterceptors);
			target.addReferencedForeignType(std.javaUtilList);
			target.addReferencedForeignType(std.accessDeniedException);
			target.addReferencedForeignType(std.entityNotFoundException);
			target.addReferencedForeignType(std.entityInstantiationException);
			target.addReferencedForeignType(std.invalidIdException);
			target.addReferencedForeignType(std.invalidFieldException);
			target.addReferencedForeignType(std.notYetImplementedException);
			target.addReferencedForeignType(std.querySyntaxException);
			target.addReferencedForeignType(std.javaxWsRsGET);
			target.addReferencedForeignType(std.javaxWsRsPOST);
			target.addReferencedForeignType(std.javaxWsRsProduces);
			target.addReferencedForeignType(std.javaxWsRsConsumes);
			target.addReferencedForeignType(std.javaxWsRsPathParam);
			target.addReferencedForeignType(std.javaxWsRsQueryParam);
			target.addReferencedForeignType(std.javaxWsRsCoreMediaType);
			target.addReferencedForeignType(std.querySpec);

			if (me.isCombinedId()) {
				target.addReferencedForeignType(me.getCombinedIdType());
			}

			addFullServiceBeanField(target, me);
			
			target.setProperty("svcBean", svcBean);
			target.setProperty("entity", me);
			target.setProperty("dto", dto);
		}
		
		
		return targetModel;
	}
}
