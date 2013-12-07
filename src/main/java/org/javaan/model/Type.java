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

import org.apache.bcel.classfile.JavaClass;

public abstract class Type extends NamedObjectBase {
	
	public enum JavaType {
		CLASS,
		INTERFACE
	}

	private final JavaClass javaClass;
	
	private final String filePath;
	
	protected Type(String name) {
		super(name);
		this.javaClass = null;
		this.filePath = null;
	}

	protected Type(JavaClass javaClass, String filePath) {
		super(javaClass.getClassName());
		this.javaClass = javaClass;
		this.filePath = filePath;
	}
	
	public static Type create(JavaClass javaClass, String filePath) {
		if (javaClass.isInterface()) {
			return new Interface(javaClass, filePath);
		} else if (javaClass.isClass()) {
			return new Clazz(javaClass, filePath);
		}
		throw new IllegalArgumentException("Unknown type: " + javaClass.toString());
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public abstract JavaType getJavaType();
}
