package org.javaan.model;

import java.lang.reflect.Modifier;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.javaan.bytecode.SignatureUtil;

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

public class Method extends NamedObjectBase {
	
	private final Type type;
	
	private final String signature;
	
	private final boolean isAbstract;
	
	private final String fullName;
	
	private org.apache.bcel.classfile.Method javaMethod;
	
	/**
	 * Constructor used by unit tests
	 */
	public Method(Type type, String signature) {
		super(buildUniqueMethodName(type, signature));
		this.type = type;
		this.signature = signature;
		this.isAbstract = false;
		this.fullName = buildFullName(type, this.name);
		
	}
	
	private Method(String name, Type type, String signature, boolean isAbstract, String fullName, org.apache.bcel.classfile.Method javaMethod) {
		this(name, type, signature, isAbstract, fullName);
		this.javaMethod = javaMethod;
	}
	
	private Method(String name, Type type, String signature, boolean isAbstract, String fullName) {
		super(name);
		this.type = type;
		this.signature = signature;
		this.isAbstract = isAbstract;
		this.fullName = fullName;
	}
	
	public static Method create(Type type, org.apache.bcel.classfile.Method javaMethod) {
		String signature = SignatureUtil.createSignature(javaMethod);
		String name = buildUniqueMethodName(type, signature);
		boolean isAbstract = javaMethod.isAbstract();
		String fullName = buildFullName(type, javaMethod);
		return new Method(name, type, signature, isAbstract, fullName, javaMethod);
	}
	
	public static Method create(Type type, java.lang.reflect.Method method) {
		String signature = SignatureUtil.createSignature(method);
		String name = buildUniqueMethodName(type, signature);
		boolean isAbstract = Modifier.isAbstract(method.getModifiers());
		String fullName = buildFullName(type, method);
		return new Method(name, type, signature, isAbstract, fullName);
	}

	private static String buildUniqueMethodName(Type type, String signature) {
		String prefix = null;
		switch (type.getJavaType()) {
		case CLASS:
			prefix = "[C]";
			break;
		case INTERFACE:
			prefix = "[I]";
			break;
		default:
			break;
		}
		
		return prefix + type.getName() + " - " + signature;
	}
	
	private static String buildFullName(Type type, String fullSignature) {
		String typeName = type.getName();
		return String.format("%s - %s", typeName, fullSignature);
	}
	
	private static String buildFullName(Type type, org.apache.bcel.classfile.Method javaMethod) {
		return buildFullName(type, javaMethod.toString());
	}
	
	private static String buildFullName(Type type, java.lang.reflect.Method method) {
		return buildFullName(type, method.toGenericString());
	}

	public Type getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}

	public MethodGen createMethodGen(Clazz clazz, ConstantPoolGen constantPoolGen) {
		return new MethodGen(javaMethod, clazz.getName(), constantPoolGen);
	}
}
