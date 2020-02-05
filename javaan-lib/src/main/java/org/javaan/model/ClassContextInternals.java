package org.javaan.model;

import org.javaan.graph.ExtendedGraph;
import org.javaan.graph.ParentChildMap;
import org.javaan.graph.Tree;

/**
 * Internal data structure of {@link ClassContext}. Used by ClassContextBuilder to produce
 * {@link ClassContext} without using (slow) ClassContext API.
 */
public class ClassContextInternals {

    public NamedObjectMap<Type> types;

    public ParentChildMap<Package, Type> typesOfPackage;

    public Tree<Clazz, String> superClass;

    public ExtendedGraph<Interface, String> superInterface;

    public ParentChildMap<Clazz, Interface> interfacesOfClass;

    public ParentChildMap<Interface, Clazz> implementationOfInterface;

    public ParentChildMap<Clazz, Method> methodsOfClass;

    public ParentChildMap<Interface, Method> methodsOfInterface;
}
