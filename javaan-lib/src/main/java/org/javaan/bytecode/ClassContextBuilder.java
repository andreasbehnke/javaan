package org.javaan.bytecode;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.NamedObjectMap;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates class context from bytecode, references to classes outside the loaded libraries are resolved
 * using {@link ReflectionClassContextBuilder}.
 */
public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private Set<String> missingTypes;

	private void processInterface(Interface interfaze, NamedObjectMap<Type> typeLookup, ClassContext classContext) {
        for (String superInterface: interfaze.getSuperInterfaceNames()) {
            classContext.addSuperInterface(interfaze, typeLookup.get(superInterface).toInterface());
        }
    }

	private void processClass(Clazz clazz, NamedObjectMap<Type> typeLookup, ClassContext classContext) {
	    String superTypeName = clazz.getSuperTypeName();
	    if (typeLookup.contains(superTypeName)) {
            classContext.addSuperClass(clazz, typeLookup.get(superTypeName).toClazz());
        }
        for (String interfaceName: clazz.getInterfaceNames()) {
	        if (typeLookup.contains(interfaceName)) {
                classContext.addInterfaceOfClass(clazz, typeLookup.get(interfaceName).toInterface());
            }
        }
    }

    private void processDependencies(Type type, NamedObjectMap<Type> typeLookup, ClassContext classContext) {
	    switch (type.getJavaType()) {
            case CLASS:
                processClass(type.toClazz(), typeLookup, classContext);
                break;
            case INTERFACE:
                processInterface(type.toInterface(), typeLookup, classContext);
                break;
        }
    }

    private void addType(Type type, ClassContext classContext) {
	    if(type.getJavaType() == Type.JavaType.CLASS) {
	        classContext.addClass(type.toClazz());
        } else {
	        classContext.addInterface(type.toInterface());
        }
        if (!type.isReflection()) {
            for (Method method : type.getJavaClass().getMethods()) {
                classContext.addMethod(org.javaan.model.Method.create(type, method));
            }
        } else {
	        for (java.lang.reflect.Method method : type.getReflectionClass().getDeclaredMethods()) {
	            classContext.addMethod(org.javaan.model.Method.create(type, method));
            }
            for (Constructor<?> constructor : type.getReflectionClass().getConstructors()) {
                classContext.addMethod(org.javaan.model.Method.create(type, constructor));
            }
        }
    }

	public ClassContext build(List<Type> types) {
        LOG.info("Creating class context ...");
		Date start = new Date();
        ReflectionTypeLoader loader = new ReflectionTypeLoader();
		types = loader.loadMissingTypes(types);
		missingTypes = loader.getMissingTypes();
		NamedObjectMap<Type> typeLookup = new NamedObjectMap<>(types);
		ClassContext context = new ClassContext();
		types.stream().forEach(type -> addType(type, context));
		types.stream ().forEach(type -> processDependencies(type, typeLookup, context));
        long duration = new Date().getTime() - start.getTime();
        LOG.info("Creation of class context with {} classes and {} interfaces took {} ms",
				context.getClasses().size(), context.getInterfaces().size(), duration);
		int numberOfMissingTypes = missingTypes.size();
		if (numberOfMissingTypes > 0) {
			LOG.warn("Missing types: {} types could not be resoled", numberOfMissingTypes);
		}
		return context;
	}
	
	public Set<String> getMissingTypes() {
		return missingTypes;
	}
}
