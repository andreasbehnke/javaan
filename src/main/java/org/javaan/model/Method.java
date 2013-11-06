package org.javaan.model;

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
