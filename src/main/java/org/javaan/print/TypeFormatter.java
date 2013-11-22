package org.javaan.print;

import org.javaan.model.Type;

public class TypeFormatter implements ObjectFormatter<Type> {

	@Override
	public String format(Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			return "[C]" + type.getName();
		case INTERFACE:
			return "[I]" + type.getName();
		default:
			throw new IllegalArgumentException("Unknown java type: " + type); // this should never happen
		}
	}
}
