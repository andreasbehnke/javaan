package org.javaan.model;

public class Method extends NamedObjectBase {
	
	private final Type type;
	
	private final org.apache.bcel.classfile.Method javaMethod;
	
	private final String signature;

	public Method(Type type, org.apache.bcel.classfile.Method javaMethod, String signature) {
		super(buildMethodName(type, signature));
		this.javaMethod = javaMethod;
		this.signature = signature;
		this.type = type;
	}

	private static String buildMethodName(NamedObjectBase type, String signature) {
		return type.getName() + " - " + signature;
	}

	public Type getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}
	
	public org.apache.bcel.classfile.Method getJavaMethod() {
		return javaMethod;
	}
}
