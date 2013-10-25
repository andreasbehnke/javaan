package org.javaan.model;

public class Clazz extends Type {

	private Clazz(String name) {
		super(name);
	}

	public static Clazz get(String name) {
		return new Clazz(name);
	}
}
