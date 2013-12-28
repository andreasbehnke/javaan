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
	
	public static final String CONSTRUCTOR_SIGNATURE = "<init>";
	
	public static String createClassSignature(Type type) {
		byte typeCode = type.getType();
		switch (typeCode) {
		case Constants.T_OBJECT:
			ObjectType obj = (ObjectType)type;
			return obj.getClassName();
		case Constants.T_ARRAY:
			ArrayType arr = (ArrayType)type;
			StringBuilder name = new StringBuilder(createClassSignature(arr.getBasicType()));
			for(int i = 0; i < arr.getDimensions(); i++) {
				name.append("[]");
			}
			return name.toString();
		default:
			return Constants.TYPE_NAMES[typeCode];
		}
	}
	
	public static List<String> createClassSignatures(Type[] types) {
		List<String> typeNames = new ArrayList<String>();
		for (Type type : types) {
			typeNames.add(createClassSignature(type));
		}
		return typeNames;
	}
	
	public static String createClassSignature(Class<?> cls) {
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
	
	public static List<String> createClassSignatures(Class<?>[] classes) {
        List<String> classNames = new ArrayList<String>(classes.length);
        for (Class<?> cls : classes) {
        	classNames.add(createClassSignature(cls));
        }
        return classNames;
    }

	private static String createMethodSignature(String methodName, List<String> methodParameterTypes) {
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
	
	public static String createMethodSignature(java.lang.reflect.Method method) {
		return createMethodSignature(
				method.getName(), 
				createClassSignatures(method.getParameterTypes()));
	}
	
	public static String createMethodSignature(Constructor<?> constructor) {
		return createMethodSignature(
				CONSTRUCTOR_SIGNATURE,
				createClassSignatures(constructor.getParameterTypes()));
	}

	public static String createMethodSignature(Method method) {
		return createMethodSignature(
				method.getName(),
				createClassSignatures(method.getArgumentTypes()));
	}
	
	public static String createMethodSignature(InvokeInstruction invoke, ConstantPoolGen cpg) {
		return createMethodSignature(
				invoke.getMethodName(cpg),
				createClassSignatures(invoke.getArgumentTypes(cpg)));
	}
}
