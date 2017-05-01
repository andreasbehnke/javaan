package org.javaan.model;

import org.javaan.graph.*;

/**
 * Internal data structure of {@link ClassContext}. Used by ClassContextBuilder to produce
 * {@link ClassContext} without using (slow) ClassContext API.
 */
public class ClassContextInternals {

    public NamedObjectMap<Type> types;

    public ParentChildMap<Package, Type> typesOfPackage;

    public Tree<Clazz, VertexEdge<Clazz>> superClass;

    public ExtendedDirectedGraph<Interface, VertexEdge<Interface>> superInterface;

    public ParentChildMap<Clazz, Interface> interfacesOfClass;

    public ParentChildMap<Interface, Clazz> implementationOfInterface;

    public ParentChildMap<Clazz, Method> methodsOfClass;

    public ParentChildMap<Interface, Method> methodsOfInterface;
}
