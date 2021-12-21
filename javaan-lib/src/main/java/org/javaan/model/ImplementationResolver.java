package org.javaan.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Resolves methods by finding their implementation
 */
public class ImplementationResolver implements MethodResolver {

	private final ClassContext classContext;

	public ImplementationResolver(ClassContext classContext) {
		this.classContext = classContext;
	}

	private Set<Type> findImplementations(Clazz clazz, Method method) {
		Set<Type> implementations = new HashSet<>();
		if (method.isAbstract()) {
			// find implementations of abstract method
			implementations.addAll(classContext.getSpecializationsOfClass(clazz));
		} else {
			implementations.add(clazz);
		}
		return implementations;
	}

	private Set<Type> findImplementations(Interface interfaze) {
		// find implementations of interface
		return new HashSet<>(classContext.getImplementations(interfaze));
	}

	@Override
	public Set<Type> resolve(Method method) {
		Type type = method.getType();
		switch (type.getJavaType()) {
		case CLASS:
			return findImplementations((Clazz)type, method);
		case INTERFACE:
			return findImplementations((Interface)type);
		default:
			throw new IllegalArgumentException("Unknown java type: " + type.getJavaType());
		}
	}

}
