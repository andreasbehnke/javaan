package org.javaan.model;

public class Method extends Type {
	
	private final Clazz clazz;
	
	private final String signature;

	public Method(Clazz clazz, String signature) {
		super(buildMethodName(clazz, signature));
		this.signature = signature;
		this.clazz = clazz;
	}

	private static String buildMethodName(Clazz clazz, String signature) {
		return clazz.getName() + " - " + signature;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public String getSignature() {
		return signature;
	}
}
