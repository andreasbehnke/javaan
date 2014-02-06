package org.javaan.model;

import java.util.HashSet;
import java.util.Set;

public class DeclaringResolver implements MethodResolver {

	@Override
	public Set<Type> resolve(Method method) {
		Set<Type> declarations = new HashSet<>();
		declarations.add(method.getType());
		return declarations;
	}

}
