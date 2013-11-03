package org.javaan.model;

public class Method extends NamedObjectBase {
	
	private final Type type;
	
	private final String signature;

	public Method(Type type, String signature) {
		super(buildMethodName(type, signature));
		this.signature = signature;
		this.type = type;
	}

	private static String buildMethodName(NamedObjectBase type, String signature) {
		return type.getName() + " - " + signature;
	}

	public NamedObjectBase getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}
}
