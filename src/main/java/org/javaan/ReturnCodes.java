package org.javaan;

public enum ReturnCodes {
	
	ok(0),
	errorParse(1),
	errorCommand(2);

	private final int value;
	
	private ReturnCodes(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
