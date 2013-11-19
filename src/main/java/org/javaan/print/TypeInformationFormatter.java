package org.javaan.print;

import org.javaan.model.Type;

public class TypeInformationFormatter implements ObjectFormatter<Type> {

	@Override
	public String format(Type type) {
		return String.format("%s [%s]", type.getName(), type.getFilePath());
	}
}
