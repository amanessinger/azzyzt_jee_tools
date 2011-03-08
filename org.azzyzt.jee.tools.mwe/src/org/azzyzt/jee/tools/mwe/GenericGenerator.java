package org.azzyzt.jee.tools.mwe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.azzyzt.jee.tools.mwe.builder.GenericBuilder;
import org.azzyzt.jee.tools.mwe.generator.JavaGenerator;
import org.azzyzt.jee.tools.mwe.model.MetaModel;

public class GenericGenerator {

	public static void main(String[] args) 
	throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
		IllegalArgumentException, InstantiationException, IllegalAccessException, 
		InvocationTargetException 
	{
        if (args.length != 3) {
            System.err.println("usage: GenericGenerator <absolute-sourcefolder> <package-name> <builder-class>");
            System.exit(1);
        }
        String sourceFolder = args[0];
        String packageName = args[1];
        String builderClazzName = args[2];
        
        Class<?> builderClazz = Class.forName(builderClazzName);
        Constructor<?> constructor = builderClazz.getConstructor(String.class);
        Object o = constructor.newInstance(packageName);
        if (!(o instanceof GenericBuilder)) {
        	System.err.println("Class "+builderClazzName+" is not a GenericBuilder");
            System.exit(1);
        }
        GenericBuilder b = (GenericBuilder)o;
        
        MetaModel targetModel = b.build();
        JavaGenerator targetGen = new JavaGenerator(targetModel, sourceFolder, b.getTemplateGroup());
		targetGen.setGenerateFields(b.getGenerateFields());
		targetGen.setGenerateDefaultConstructor(b.getGenerateDefaultConstructor());
		targetGen.setGenerateGettersSetters(b.getGenerateGettersSetters());
		int numberGenerated = targetGen.generate();
        
		System.err.println("Generated "+numberGenerated+" sources");
	}

}
