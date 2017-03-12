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

	private final Class clazz;

	private final JavaClass javaClass;

    private final String filePath;

	private final boolean isReflection;
	
	protected Type(String name) {
		super(name);
		this.clazz = null;
		this.javaClass = null;
		this.filePath = null;
        this.isReflection = false;
	}

	protected Type(JavaClass javaClass, String filePath) {
		super(javaClass.getClassName());
        this.clazz = null;
		this.javaClass = javaClass;
		this.filePath = filePath;
        this.isReflection = false;
	}

	protected Type(Class clazz) {
	    super(clazz.getName());
	    this.clazz = clazz;
        this.javaClass = null;
        this.filePath = null;
        this.isReflection = true;
    }
	
	public static Type create(JavaClass javaClass, String filePath) {
		if (javaClass.isInterface()) {
			return new Interface(javaClass, filePath);
		} else if (javaClass.isClass()) {
			return new Clazz(javaClass, filePath);
		}
		throw new IllegalArgumentException("Unknown type: " + javaClass.toString());
	}

	public static Type create(String className) throws ClassNotFoundException {
        Type type = null;
        Class<?> clazz = Class.forName(className);
        if (clazz.isInterface()) {
            type = new Interface(clazz);
        } else {
            type = new Clazz(clazz);
        }
        return type;
	}
	
	public JavaClass getJavaClass() {
		return javaClass;
	}

	public String getFilePath() {
		return filePath;
	}

    public boolean isReflection() {
        return isReflection;
    }

    public static String getShortName(String signature) {
		int index = signature.lastIndexOf('.');
		if (index > 0) {
			return signature.substring(index + 1);
		}
		return signature;
	}
	
	public String getShortName() {
		return getShortName(getName());
	}
	
	public abstract JavaType getJavaType();
	
	public Clazz toClazz() {
		return (Clazz)this;
	}
	
	public Interface toInterface() {
		return (Interface)this;
	}
}
