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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 * Creates unique method signatures from methods or invocations
 */
public class SignatureUtil {
	
	private static final String CONSTRUCTOR_SIGNATURE = "<init>";

	private static String getClassSignature(Type type) {
		byte typeCode = type.getType();
		switch (typeCode) {
		case Constants.T_OBJECT:
			ObjectType obj = (ObjectType)type;
			return obj.getClassName();
		case Constants.T_ARRAY:
			ArrayType arr = (ArrayType)type;
			StringBuilder name = new StringBuilder(getClassSignature(arr.getBasicType()));
			for(int i = 0; i < arr.getDimensions(); i++) {
				name.append("[]");
			}
			return name.toString();
		default:
			return Constants.TYPE_NAMES[typeCode];
		}
	}
	
	private static List<String> convertTypesToClassSignatures(Type[] types) {
		List<String> typeNames = new ArrayList<String>();
		for (Type type : types) {
			typeNames.add(getClassSignature(type));
		}
		return typeNames;
	}
	
	private static String getClassSignature(Class<?> cls) {
		if (cls.isArray()) {
			StringBuilder dimension = new StringBuilder("[]");
			Class<?> comp = cls.getComponentType();
			while(comp.isArray()) {
				dimension.append("[]");
				comp = comp.getComponentType();
			}
			return comp.getName() + dimension.toString();
    	} else {
    		return cls.getName();
    	}
	}
	
	private static List<String> convertClassesToClassSignatures(List<Class<?>> classes) {
        List<String> classNames = new ArrayList<String>(classes.size());
        for (Class<?> cls : classes) {
        	classNames.add(getClassSignature(cls));
        }
        return classNames;
    }

	private static String createSignature(String methodName, List<String> methodParameterTypes) {
		StringBuilder builder = new StringBuilder();
		builder.append(methodName).append('(');
		for (int i = 0; i < methodParameterTypes.size(); i++) {
			builder.append(methodParameterTypes.get(i));
			if (i < methodParameterTypes.size() - 1) {
				builder.append(',');
			}
		}
		builder.append(')');
		return builder.toString();
	}
	
	public static String createSignature(java.lang.reflect.Method method) {
		return createSignature(
				method.getName(), 
				convertClassesToClassSignatures(Arrays.asList(method.getParameterTypes())));
	}
	
	public static String createSignature(Constructor<?> constructor) {
		return createSignature(
				CONSTRUCTOR_SIGNATURE,
				convertClassesToClassSignatures(Arrays.asList(constructor.getParameterTypes())));
	}

	public static String createSignature(Method method) {
		return createSignature(
				method.getName(),
				convertTypesToClassSignatures(method.getArgumentTypes()));
	}
	
	public static String createSignature(InvokeInstruction invoke, ConstantPoolGen cpg) {
		return createSignature(
				invoke.getMethodName(cpg),
				convertTypesToClassSignatures(invoke.getArgumentTypes(cpg)));
	}
}
