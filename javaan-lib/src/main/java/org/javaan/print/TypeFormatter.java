package org.javaan.print;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Type;

public class TypeFormatter implements ObjectFormatter<Type> {

	private ObjectFormatter<Clazz> clazzFormatter;
	
	private ObjectFormatter<Interface> interfaceFormatter;
	
	public TypeFormatter() {
		this.clazzFormatter = new ClazzFormatter();
		this.interfaceFormatter = new InterfaceFormatter();
	}
	
	public TypeFormatter(ObjectFormatter<Clazz> clazzFormatter, ObjectFormatter<Interface> interfaceFormatter) {
		this.clazzFormatter = clazzFormatter;
		this.interfaceFormatter = interfaceFormatter;
	}

	@Override
	public String format(Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			return clazzFormatter.format(type.toClazz());
		case INTERFACE:
			return interfaceFormatter.format(type.toInterface());
		default:
			throw new IllegalArgumentException("Unknown java type: " + type.getJavaType());
		}
	}

}
