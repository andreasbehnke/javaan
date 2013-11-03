package org.javaan.model;

import org.apache.bcel.classfile.JavaClass;

public class Clazz extends Type {

	public Clazz(String name) {
		super(name);
	}
	
	protected Clazz(JavaClass javaClass, String filePath) {
		super(javaClass, filePath);
	}
	
	@Override
	public JavaType getJavaType() {
		return JavaType.CLASS;
	}
}
