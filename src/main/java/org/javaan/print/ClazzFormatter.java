package org.javaan.print;

import org.javaan.model.Clazz;

public class ClazzFormatter implements ObjectFormatter<Clazz> {
	
	@Override
	public String format(Clazz object) {
		return "[C]" + object.getName();
	}
}
