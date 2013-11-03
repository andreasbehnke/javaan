package org.javaan.model;

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
	
	public Interface createInterface() {
		return new Interface(javaClass, filePath);
	}
	
	public Clazz createClass() {
		return new Clazz(javaClass, filePath);
	}
	
	public abstract JavaType getJavaType();
}
