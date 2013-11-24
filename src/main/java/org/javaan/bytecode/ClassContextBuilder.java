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

import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.NamedObjectRepository;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private final NamedObjectRepository<Type> types;
	
	public ClassContextBuilder(List<Type> types) {
		this.types = new NamedObjectRepository<Type>(types);
	}
	
	private Interface getInterface(String name) {
		Interface interfaze = (Interface)types.get(name);
		if (interfaze == null) {
			interfaze =  new Interface(name);
		}
		return interfaze;
	}
	
	private Clazz getClazz(String name) {
		Clazz clazz = (Clazz)types.get(name);
		if (clazz == null) {
			clazz =  new Clazz(name);
		}
		return clazz;
	}
	
	private org.javaan.model.Method createMethod(Interface interfaze, Method method) {
		return new org.javaan.model.Method(interfaze, method, SignatureUtil.createSignature(method));
	}
	
	private org.javaan.model.Method createMethod(Clazz clazz, Method method) {
		return new org.javaan.model.Method(clazz, method, SignatureUtil.createSignature(method));
	}
	
	private void addType(ClassContext context, Interface interfaze) {
		JavaClass javaClass = interfaze.getJavaClass();
		String[] interfaces = javaClass.getInterfaceNames();
		context.addInterface(interfaze);
		for (String superInterfaceName : interfaces) {
			context.addSuperInterface(interfaze, getInterface(superInterfaceName));
		}
		for (Method method : javaClass.getMethods()) {
			context.addMethod(createMethod(interfaze, method));
		}
	}
	
	private void addType(ClassContext context, Clazz clazz) {
		JavaClass javaClass = clazz.getJavaClass();
		String superClassName = javaClass.getSuperclassName();
		if (superClassName == null || "java.lang.Object".equals(superClassName)) {
			context.addClass(clazz);
		} else {
			context.addSuperClass(clazz, getClazz(superClassName));
		}
		String[] interfaceNames = javaClass.getInterfaceNames();
		if (interfaceNames != null) {
			for (String interfaceName : interfaceNames) {
				Interface interfaze = getInterface(interfaceName);
				context.addInterface(interfaze);
				context.addInterfaceOfClass(clazz, interfaze);
			}
		}
		for (Method method : javaClass.getMethods()) {
			context.addMethod(createMethod(clazz, method));
		}
	}
	
	private void addType(ClassContext context, Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			addType(context, (Clazz)type);
			break;
		case INTERFACE:
			addType(context, (Interface)type);
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
