package org.javaan.print;

import org.javaan.model.Method;

public class MethodFormatter implements ObjectFormatter<Method> {

	@Override
	public String format(Method method) {
		String signature = method.getJavaMethod().toString();
		String typeName = method.getType().getName();
		return String.format("%s - %s", typeName, signature);
	}

}
