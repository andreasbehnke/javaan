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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Method;
import org.javaan.model.NamedObjectMap;
import org.javaan.model.NamedObjectRepository;
import org.javaan.model.Type;

/**
 * Provides methods for adding {@link Interface}s and {@link Clazz}es to {@link ClassContext}.
 * Tries to resolve references using the provided {@link NamedObjectMap} types. If a type
 * is not found in that repository, tries to resolve type using java reflection and type loading.
 */
class ReflectionClassContextBuilder {
	
	private static final String JAVA_LANG_OBJECT = "java.lang.Object";

	private final ClassContext context;
	
	private final NamedObjectRepository<Type> types;
	
	private final Set<String> missingTypes = new HashSet<String>();

	public ReflectionClassContextBuilder(ClassContext context, NamedObjectRepository<Type> types) {
		this.context = context;
		this.types = types;
	}

	public Set<String> getMissingTypes() {
		return missingTypes;
	}

	private void addMethods(Type type, Class<?> clazz) {
		clazz.getMethods();
		for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
			context.addMethod(Method.create(type, method));
		}
		for (Constructor<?> constructor : clazz.getConstructors()) {
			context.addMethod(Method.create(type, constructor));
		}
	}

	private Type createTypeFromClass(String className) {
		Type type = null;
		try {
			Class<?> clazz = Class.forName(className);
			if (clazz.isInterface()) {
				type = new Interface(className);
				Class<?>[] superInterfaces = clazz.getInterfaces();
				addInterface((Interface)type, ClassUtils.convertClassesToClassNames(Arrays.asList(superInterfaces)));
				addMethods(type, clazz);
			} else {
				type = new Clazz(className);
				Class<?> superClass = clazz.getSuperclass();
				String superClassName = (superClass == null) ? null : superClass.getName();
				Class<?>[] implementedInterfaces = clazz.getInterfaces();
				addClass((Clazz)type, superClassName, ClassUtils.convertClassesToClassNames(Arrays.asList(implementedInterfaces)));
				addMethods(type, clazz);
			}
		} catch (ClassNotFoundException e) {
			missingTypes.add(className);
			return null;
		}
		return type;
	}
	
	public Type getType(String name) {
		if (missingTypes.contains(name)) {
			return null;
		}
		Type type = types.get(name);
		if (type == null) {
			type = context.get(name);
		}
		if (type == null) {
			type = createTypeFromClass(name);
		}
		return type;
	}

	public void addClass(Clazz clazz, String superClassName, List<String> interfaceNames) {
		if (superClassName == null) {
			superClassName = JAVA_LANG_OBJECT;
		}
		if (JAVA_LANG_OBJECT.equals(clazz.getName())) {
			context.addClass(clazz);
		} else {
			Clazz superClass = (Clazz)getType(superClassName);
			context.addClass(clazz);
			if (superClass != null) {
				context.addClass(superClass);
				context.addSuperClass(clazz, superClass);
			}
		}
		if (interfaceNames != null) {
			for (String interfaceName : interfaceNames) {
				Interface interfaze = (Interface)getType(interfaceName);
				if (interfaze != null) {
					context.addInterface(interfaze);
					context.addInterfaceOfClass(clazz, interfaze);
				}
			}
		}
	}
	
	public void addInterface(Interface interfaze, List<String> superInterfaces) {
		context.addInterface(interfaze);
		for (String superInterfaceName : superInterfaces) {
			Interface superInterface = (Interface)getType(superInterfaceName);
			if (superInterface != null) {
				context.addInterface(superInterface);
				context.addSuperInterface(interfaze, superInterface);
			}
		}
	}
}
