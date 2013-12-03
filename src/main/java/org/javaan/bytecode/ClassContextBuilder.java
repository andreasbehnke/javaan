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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.commons.lang3.ClassUtils;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.NamedObjectRepository;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates class context from bytecode, references to classes outside the loaded libraries are resolved
 * using classloader.
 */
public class ClassContextBuilder {
	
	private static final String JAVA_LANG_OBJECT = "java.lang.Object";

	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private final NamedObjectRepository<Type> types;
	
	private final Set<String> missingTypes;
	
	public ClassContextBuilder(List<Type> types) {
		this.types = new NamedObjectRepository<Type>(types);
		this.missingTypes = new HashSet<String>();
	}
	
	public Set<String> getMissingTypes() {
		return missingTypes;
	}
	
	private org.javaan.model.Method createMethod(Type type, java.lang.reflect.Method method) {
		return new org.javaan.model.Method(type, null, SignatureUtil.createSignature(method));
	}
	
	private void addMethods(ClassContext context, Type type, Class<?> clazz) {
		clazz.getMethods();
		for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
			context.addMethod(createMethod(type, method));
		}
	}
	
	private Type createTypeFromClass(ClassContext context, String className) {
		ClassLoader classLoader = getClass().getClassLoader();
		Type type = null;
		try {
			Class<?> clazz = classLoader.loadClass(className);
			if (clazz.isInterface()) {
				type = new Interface(className);
				Class<?>[] superInterfaces = clazz.getInterfaces();
				addInterface(context, (Interface)type, ClassUtils.convertClassesToClassNames(Arrays.asList(superInterfaces)));
				addMethods(context, type, clazz);
			} else {
				type = new Clazz(className);
				Class<?> superClass = clazz.getSuperclass();
				String superClassName = (superClass == null) ? null : superClass.getName();
				Class<?>[] implementedInterfaces = clazz.getInterfaces();
				addClass(context, (Clazz)type, superClassName, ClassUtils.convertClassesToClassNames(Arrays.asList(implementedInterfaces)));
				addMethods(context, type, clazz);
			}
		} catch (ClassNotFoundException e) {
			LOG.warn("Could not resolve reference to external type: ", className);
			missingTypes.add(className);
			return null;
		}
		return type;
	}
	
	private Interface getInterface(ClassContext context, String name) {
		Interface interfaze = (Interface)types.get(name);
		if (interfaze == null) {
			return (Interface)createTypeFromClass(context, name);
		}
		return interfaze;
	}
	
	private Clazz getClazz(ClassContext context, String name) {
		Clazz clazz = (Clazz)types.get(name);
		if (clazz == null) {
			return (Clazz)createTypeFromClass(context, name);
		}
		return clazz;
	}
	
	private org.javaan.model.Method createMethod(Type type, Method method) {
		return new org.javaan.model.Method(type, method, SignatureUtil.createSignature(method));
	}
	
	private void addMethods(ClassContext context, Type type, JavaClass javaClass) {
		for (Method method : javaClass.getMethods()) {
			context.addMethod(createMethod(type, method));
		}
	}
	
	private void addInterface(ClassContext context, Interface interfaze, List<String> superInterfaces) {
		context.addInterface(interfaze);
		for (String superInterfaceName : superInterfaces) {
			context.addSuperInterface(interfaze, getInterface(context, superInterfaceName));
		}
	}
	
	private void addInterface(ClassContext context, Interface interfaze, JavaClass javaClass) {
		String[] superInterfaces = javaClass.getInterfaceNames();
		addInterface(context, interfaze, Arrays.asList(superInterfaces));
		addMethods(context, interfaze, javaClass);
	}
	
	private void addClass(ClassContext context, Clazz clazz, String superClassName, List<String> interfaceNames) {
		if (superClassName == null) {
			superClassName = JAVA_LANG_OBJECT;
		}
		if (JAVA_LANG_OBJECT.equals(clazz.getName())) {
			context.addClass(clazz);
		} else {
			context.addSuperClass(clazz, getClazz(context, superClassName));
		}
		if (interfaceNames != null) {
			for (String interfaceName : interfaceNames) {
				Interface interfaze = getInterface(context, interfaceName);
				context.addInterface(interfaze);
				context.addInterfaceOfClass(clazz, interfaze);
			}
		}
	}
	
	private void addClass(ClassContext context, Clazz clazz, JavaClass javaClass) {
		String superClassName = javaClass.getSuperclassName();
		String[] interfaceNames = javaClass.getInterfaceNames();
		addClass(context, clazz, superClassName, Arrays.asList(interfaceNames));
		addMethods(context, clazz, javaClass);
	}
	
	private void addType(ClassContext context, Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			addClass(context, (Clazz)type, type.getJavaClass());
			break;
		case INTERFACE:
			addInterface(context, (Interface)type, type.getJavaClass());
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + type.getJavaType());
		}
	}
	
	public ClassContext build() {
		LOG.info("Creating class context ...");
		ClassContext context = new ClassContext();
		for (Type type : types.getNamedObjects()) {
			addType(context, type);
		}
		LOG.info("Created class context with {} classes and {} interfaces", context.getClasses().size(), context.getInterfaces().size());
		return context;
	}
}
