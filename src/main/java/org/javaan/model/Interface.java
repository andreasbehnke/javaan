package org.javaan.model;

public class Interface extends Type {

	private Interface(String name) {
		super(name);
	}

	public static Interface get(String name) {
		return new Interface(name);
	}
}
