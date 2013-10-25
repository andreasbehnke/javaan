package org.javaan.model;

public class Method extends Type {
	
	private final Type type;
	
	private final String signature;

	private Method(Type type, String signature) {
		super(buildMethodName(type, signature));
		this.signature = signature;
		this.type = type;
	}

	private static String buildMethodName(Type type, String signature) {
		return type.getName() + " - " + signature;
	}

	public Type getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}

	public static Method get(Type type, String signature) {
		return new Method(type, signature);
	}
}
