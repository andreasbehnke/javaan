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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang3.ClassUtils;

/**
 * Creates unique method signatures from methods or invocations
 */
public class SignatureUtil {

	private static String getClassName(Type type) {
		byte typeCode = type.getType();
		if (typeCode == Constants.T_OBJECT) {
			ObjectType obj = (ObjectType)type;
			return obj.getClassName();
		} else {
			return Constants.TYPE_NAMES[typeCode];
		}
	}
	
	private static List<String> convertTypesToClassNames(Type[] types) {
		List<String> typeNames = new ArrayList<String>();
		for (Type type : types) {
			typeNames.add(getClassName(type));
		}
		return typeNames;
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
				ClassUtils.convertClassesToClassNames(Arrays.asList(method.getParameterTypes())));
	}

	public static String createSignature(Method method) {
		return createSignature(
				method.getName(),
				convertTypesToClassNames(method.getArgumentTypes()));
	}
	
	public static String createSignature(InvokeInstruction invoke, ConstantPoolGen cpg) {
		return createSignature(
				invoke.getMethodName(cpg),
				convertTypesToClassNames(invoke.getArgumentTypes(cpg)));
	}
}
