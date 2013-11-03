package org.javaan.model;

import org.apache.bcel.classfile.JavaClass;

public class Interface extends Type {

	public Interface(String name) {
		super(name);
	}
	
	protected Interface(JavaClass javaClass, String filePath) {
		super(javaClass, filePath);
	}
	
	@Override
	public JavaType getJavaType() {
		return JavaType.INTERFACE;
	}
}
