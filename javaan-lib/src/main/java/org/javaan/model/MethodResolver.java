package org.javaan.model;

import java.util.Set;

/**
 * Algorithm for finding types which own a method.
 */
public interface MethodResolver {

	Set<Type> resolve(Method method);
}
