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

import java.util.Arrays;
import java.util.List;

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

	private final ClassContext context = new ClassContext();
	
	private final ReflectionClassContextBuilder reflectionClassContextBuilder;
	
	private final NamedObjectMap<Type> types;
	
	public ClassContextBuilder(List<Type> types) {
		this.types = new NamedObjectMap<Type>(types);
		reflectionClassContextBuilder = new ReflectionClassContextBuilder(context, this.types);
	}

	private void addMethods(Type type, JavaClass javaClass) {
		for (Method method : javaClass.getMethods()) {
			context.addMethod(org.javaan.model.Method.create(type, method));
		}
	}
	
	private void addInterface(Interface interfaze, JavaClass javaClass) {
		String[] superInterfaces = javaClass.getInterfaceNames();
		reflectionClassContextBuilder.addInterface(interfaze, Arrays.asList(superInterfaces));
		addMethods(interfaze, javaClass);
	}
	
	private void addClass(Clazz clazz, JavaClass javaClass) {
		String superClassName = javaClass.getSuperclassName();
		String[] interfaceNames = javaClass.getInterfaceNames();
		reflectionClassContextBuilder.addClass(clazz, superClassName, Arrays.asList(interfaceNames));
		addMethods(clazz, javaClass);
	}
	
	private void addType(Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			addClass((Clazz)type, type.getJavaClass());
			break;
		case INTERFACE:
			addInterface((Interface)type, type.getJavaClass());
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + type.getJavaType());
		}
	}
	
	public ClassContext build() {
		LOG.info("Creating class context ...");
		for (Type type : types.getNamedObjects()) {
			addType(type);
		}
		LOG.info("Created class context with {} classes and {} interfaces", 
				new Object[]{context.getClasses().size(), context.getInterfaces().size()});
		int numberOfMissingTypes = reflectionClassContextBuilder.getMissingTypes().size();
		if (numberOfMissingTypes > 0) {
			LOG.warn("Missing types: {} types could not be resoled", numberOfMissingTypes);
		}
		return context;
	}
}
