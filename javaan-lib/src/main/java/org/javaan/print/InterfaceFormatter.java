package org.javaan.print;

import org.javaan.model.Interface;

public class InterfaceFormatter implements ObjectFormatter<Interface> {

	@Override
	public String format(Interface object) {
		return "[I]" + object.getName();
	}

}
