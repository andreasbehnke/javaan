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

public class Method extends NamedObjectBase {
	
	private final Type type;
	
	private final org.apache.bcel.classfile.Method javaMethod;
	
	private final String signature;
	
	private String fullName;

	public Method(Type type, org.apache.bcel.classfile.Method javaMethod, String signature) {
		super(buildUniqueMethodName(type, signature));
		this.javaMethod = javaMethod;
		this.signature = signature;
		this.type = type;
		if (javaMethod != null) {
			this.fullName = buildFullName(type, javaMethod);
		}
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
	
	private static String buildFullName(Type type, org.apache.bcel.classfile.Method javaMethod) {
		String signature = javaMethod.toString();
		String typeName = type.getName();
		return String.format("%s - %s", typeName, signature);
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
	
	public org.apache.bcel.classfile.Method getJavaMethod() {
		return javaMethod;
	}
}
