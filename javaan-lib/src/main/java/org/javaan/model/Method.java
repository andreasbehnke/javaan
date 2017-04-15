package org.javaan.model;

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
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.javaan.bytecode.SignatureUtil;

/**
 * Represents a java method.
 * 
 * methodName: The simple name of the method
 * signature: The signature consists of the following parts:
 *  - the full name of return type
 *  - the simple method name
 *  - "("
 *  - list of full parameter names separated by ","
 *  - ")"
 * name: The unique name, which consists of the following parts:
 * 	- Full name of declaring type
 *  - The constant string " - "
 *  - signature
 *  
 */
public class Method extends NamedObjectBase {
	
	private final Type type;
	
	private final String signature;
	
	private final String methodName;
	
	private final boolean isAbstract;
	
	private final String returnType;
	
	private final List<String> paramterTypes;
	
	private final List<String> annotationTypes;
	
	private org.apache.bcel.classfile.Method javaMethod;
	
	/**
	 * Constructor used by unit tests
	 */
	public Method(Type type, String signature) {
		super(buildUniqueMethodName(type, signature));
		this.type = type;
		this.signature = signature;
		this.isAbstract = false;
		this.returnType = null;
		this.paramterTypes = null;
		this.annotationTypes = null;
		this.methodName = null;
	}
	
	private Method(String name, Type type, String signature, String methodName, boolean isAbstract, String returnType, List<String> parameterTypes, List<String> annotationTypes, org.apache.bcel.classfile.Method javaMethod) {
		this(name, type, signature, methodName, isAbstract, returnType, parameterTypes, annotationTypes);
		this.javaMethod = javaMethod;
	}
	
	private Method(String name, Type type, String signature, String methodName, boolean isAbstract, String returnType, List<String> parameterTypes, List<String> annotationTypes) {
		super(name);
		this.type = type;
		this.signature = signature;
		this.methodName = methodName;
		this.isAbstract = isAbstract;
		this.returnType = returnType;
		this.paramterTypes = parameterTypes;
		this.annotationTypes = annotationTypes;
	}
	
	public static Method create(Type type, org.apache.bcel.classfile.Method javaMethod) {
		String signature = SignatureUtil.createMethodSignature(javaMethod);
		String name = buildUniqueMethodName(type, signature);
		String methodName = javaMethod.getName();
		boolean isAbstract = javaMethod.isAbstract();
		String returnType = SignatureUtil.createClassSignature(javaMethod.getReturnType());
		List<String> paramterTypes = SignatureUtil.createClassSignatures(javaMethod.getArgumentTypes());
		List<String> annotationTypes = SignatureUtil.createClassSignatures(javaMethod.getAnnotationEntries());
		return new Method(name, type, signature, methodName, isAbstract, returnType, paramterTypes, annotationTypes, javaMethod);
	}
	
	public static Method create(Type type, java.lang.reflect.Method method) {
		String signature = SignatureUtil.createMethodSignature(method);
		String name = buildUniqueMethodName(type, signature);
		String methodName = method.getName();
		boolean isAbstract = Modifier.isAbstract(method.getModifiers());
		String returnType = SignatureUtil.createClassSignature(method.getGenericReturnType());
		List<String> paramterTypes = SignatureUtil.createClassSignatures(method.getParameterTypes());
		List<String> annotationTypes = SignatureUtil.createClassSignatures(method.getAnnotations());
		return new Method(name, type, signature, methodName, isAbstract, returnType, paramterTypes, annotationTypes);
	}
	
	public static Method create(Type type, Constructor<?> constructor) {
		String signature = SignatureUtil.createMethodSignature(constructor);
		String name = buildUniqueMethodName(type, signature);
		boolean isAbstract = Modifier.isAbstract(constructor.getModifiers());
		String returnType = "void";
		List<String> paramterTypes = SignatureUtil.createClassSignatures(constructor.getParameterTypes());
		List<String> annotationTypes = SignatureUtil.createClassSignatures(constructor.getAnnotations());
		return new Method(name, type, signature, SignatureUtil.CONSTRUCTOR_SIGNATURE, isAbstract, returnType, paramterTypes, annotationTypes);
	}

	private static String buildUniqueMethodName(Type type, String signature) {
		return type.getName() + " - " + signature;
	}
	
	public Type getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public List<String> getParamterTypes() {
		return paramterTypes;
	}
	
	public List<String> getAnnotationTypes() {
		return annotationTypes;
	}

	public MethodGen createMethodGen() {
		return new MethodGen(javaMethod, type.getName(), type.getConstantPoolGen());
	}
}
