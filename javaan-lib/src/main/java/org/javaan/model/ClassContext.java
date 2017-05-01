package org.javaan.model;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javaan.graph.*;
import org.javaan.model.Type.JavaType;

public class ClassContext implements NamedObjectRepository<Type> {

	private final NamedObjectMap<Type> types;

	private final ParentChildMap<Package, Type> typesOfPackage;

	private final Tree<Clazz, VertexEdge<Clazz>> superClass;

	private final ExtendedDirectedGraph<Interface, VertexEdge<Interface>> superInterface;

	private final ParentChildMap<Clazz, Interface> interfacesOfClass;

	private final ParentChildMap<Interface, Clazz> implementationOfInterface;

	private final ParentChildMap<Clazz, Method> methodsOfClass;

	private final ParentChildMap<Interface, Method> methodsOfInterface;

	public ClassContext() {
		this.types = new NamedObjectMap<>();
		this.typesOfPackage = new ParentChildMap<>();
		this.superClass = GraphFactory.createVertexEdgeTree();
		this.superInterface = GraphFactory.createVertexEdgeDirectedGraph();
		this.interfacesOfClass = new ParentChildMap<>();
		this.implementationOfInterface = new ParentChildMap<>();
		this.methodsOfClass = new ParentChildMap<>();
		this.methodsOfInterface = new ParentChildMap<>();
	}

	public ClassContext(ClassContextInternals internals) {
		this.types = internals.types;
		this.typesOfPackage = internals.typesOfPackage;
		this.superClass = internals.superClass;
		this.superInterface = internals.superInterface;
		this.interfacesOfClass = internals.interfacesOfClass;
		this.implementationOfInterface = internals.implementationOfInterface;
		this.methodsOfClass = internals.methodsOfClass;
		this.methodsOfInterface = internals.methodsOfInterface;
	}

	public TreeView<Clazz, VertexEdge<Clazz>> getSuperClassGraph() {
		return superClass;
	}
	
	public GraphView<Interface, VertexEdge<Interface>> getSuperInterfaceGraph() {
		return superInterface;
	}

	/* NamedObjectRepository API */

	@Override
	public Type get(String className) {
		return types.get(className);
	}

	@Override
	public boolean contains(String name) {
		return types.contains(name);
	}

	/*****************************/

	private void addType(Type type) {
		if (type == null) {
			throw new IllegalArgumentException("Parameter type must not be null");
		}
		if (!types.contains(type.getName())) {
			types.add(type);
			typesOfPackage.addChild(new Package(type), type);
		}
	}

	public void addClass(Clazz className) {
		addType(className);
		superClass.addVertex(className);
	}

	public void addSuperClass(Clazz className, Clazz superClassName) {
		if (className == null) {
			throw new IllegalArgumentException("Parameter className must not be null");
		}
		if (superClassName == null) {
			throw new IllegalArgumentException("Parameter superClassName must not be null");
		}
		if (!types.contains(className.getName())) {
			throw new IllegalArgumentException("Unknown class " + className);
		}
		if (!types.contains(superClassName.getName())) {
			throw new IllegalArgumentException("Unknown class " + superClassName);
		}
		superClass.addEdge(superClassName, className);
	}
	
	public boolean containsClass(Clazz className) {
		return superClass.containsVertex(className);
	}
	
	public Set<Clazz> getClasses() {
		return superClass.vertexSet();
	}

	public Clazz getSuperClass(Clazz className) {
		return superClass.sourceVertexOf(className);
	}
	
	public List<Clazz> getSuperClassHierachy(Clazz className) {
		return superClass.predecessorPathOf(className);
	}
	
	public Set<Clazz> getSpecializationsOfClass(Clazz className) {
		return superClass.successorsOf(className);
	}
	
	public Set<Clazz> getDirectSpecializationsOfClass(Clazz className) {
		return superClass.targetVerticesOf(className);
	}
	
	public void addInterface(Interface interfaceName) {
		addType(interfaceName);
		superInterface.addVertex(interfaceName);
	}

	public void addSuperInterface(Interface interfaceName, Interface superInterfaceName) {
		if (interfaceName == null) {
			throw new IllegalArgumentException("Parameter interfaceName must not be null");
		}
		if (superInterfaceName == null) {
			throw new IllegalArgumentException("Parameter superInterfaceName must not be null");
		}
		if (!types.contains(interfaceName.getName())) {
			throw new IllegalArgumentException("Unknown interface " + interfaceName);
		}
		if (!types.contains(superInterfaceName.getName())) {
			throw new IllegalArgumentException("Unknown interface " + superInterfaceName);
		}
		superInterface.addEdge(interfaceName, superInterfaceName);
	}
	
	public boolean containsInterface(Interface interfaceName) {
		return superInterface.containsVertex(interfaceName);
	}
	
	public Set<Interface> getInterfaces() {
		return superInterface.vertexSet();
	}

	public Set<Interface> getSuperInterfaces(Interface interfaceName) {
		return superInterface.successorsOf(interfaceName);
	}
	
	public Set<Interface> getSpecializationOfInterface(Interface interfaceName) {
		return superInterface.predecessorsOf(interfaceName);
	}
	
	public void addInterfaceOfClass(Clazz className, Interface interfaceName) {
		if (!superInterface.containsVertex(interfaceName)) {
			throw new IllegalArgumentException("Unknown interface " + interfaceName);
		}
		if (!superClass.containsVertex(className)) {
			throw new IllegalArgumentException("Unknown class " + className);
		}
		interfacesOfClass.addChild(className, interfaceName);
		implementationOfInterface.addChild(interfaceName, className);
	}
	
	private Set<Interface> getDirectInterfacesOfClass(Clazz className) {
		Set<Interface> childs = interfacesOfClass.get(className);
		if (childs == null) return Collections.EMPTY_SET;
		Set<Interface> interfaces = new HashSet<>(childs);
		for (Interface interfaceName : childs) {
			interfaces.addAll(superInterface.successorsOf(interfaceName));
		}
		return interfaces;
	}
	
	public Set<Interface> getInterfacesOfClass(Clazz className) {
		List<Clazz> superClasses = superClass.predecessorPathOf(className);
		Set<Interface> interfaces = new HashSet<Interface>();
		for (Clazz superClassName : superClasses) {
			interfaces.addAll(getDirectInterfacesOfClass(superClassName));
		}
		return interfaces;
	}
	
	public Set<Clazz> getImplementations(Interface interfaceName) {
		Set<Clazz> implementingClasses = new HashSet<Clazz>();
		Set<Interface> interfaces = superInterface.predecessorsOf(interfaceName);
		interfaces.add(interfaceName);
		Set<Clazz> classes = new HashSet<Clazz>();
		// find direct implementations of all specialized interfaces
		for (Interface specializedInterface : interfaces) {
			Set<Clazz> implementations = implementationOfInterface.get(specializedInterface);
			if (implementations != null) classes.addAll(implementations);
		}
		// find all specializations of implementations
		for (Clazz className : classes) {
			implementingClasses.add(className);
			implementingClasses.addAll(superClass.successorsOf(className));
		}
		return implementingClasses;
	}
	
	public Set<Package> getPackages() {
		return typesOfPackage.keySet();
	}
	
	public Package getPackageOfType(Type type) {
		return new Package(type);
	}
	
	public Set<Clazz> getClassesOfPackage(Package package1) {
		Set<Clazz> classes = new HashSet<Clazz>();
		for (Type type : typesOfPackage.get(package1)) {
			if (type.getJavaType() == JavaType.CLASS) {
				classes.add((Clazz)type);
			}
		}
		return classes;
	}
	
	public Set<Interface> getInterfacesOfPackage(Package package1) {
		Set<Interface> interfaces = new HashSet<Interface>();
		for (Type type : typesOfPackage.get(package1)) {
			if (type.getJavaType() == JavaType.INTERFACE) {
				interfaces.add((Interface)type);
			}
		}
		return interfaces;
	}
	
	public Set<Type> getTypesOfPackage(Package package1) {
		return typesOfPackage.get(package1);
	}
	
	public void addMethod(Method method) {
		Type typeName = method.getType();
		switch (typeName.getJavaType()) {
		case CLASS:
			if (!superClass.containsVertex((Clazz)typeName)) {
				throw new IllegalArgumentException("Unknown class " + typeName);
			}
			methodsOfClass.addChild(typeName.toClazz(), method);
			break;
		case INTERFACE:
			if (!superInterface.containsVertex((Interface)typeName)) {
				throw new IllegalArgumentException("Unknown interface " + typeName);
			}
			methodsOfInterface.addChild(typeName.toInterface(), method);
			break;
		default:
			break;
		}
	}

	private Method findMethod(Set<Method> methods, String signature) {
		if (methods != null) {
			for (Method method : methods) {
				if (method.getSignature().equals(signature)) {
					return method;
				}
			}
		}
		return null;
	}
	
	public Method getMethod(Clazz className, String signature) {
		return findMethod(getMethods(className), signature);
	}
	
	public Method getMethod(Interface interfaceName, String signature) {
		return findMethod(getMethods(interfaceName), signature);
	}
	
	public Set<Method> getMethods() {
		return Stream.concat(methodsOfClass.values().stream().flatMap(Set::stream),
				methodsOfInterface.values().stream().flatMap(Set::stream))
				.collect(Collectors.toSet());
	}

	public Set<Method> getMethodsOfClasses() {
		return methodsOfClass.values().stream()
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}
	
	public Set<Method> getMethods(Clazz className) {
		return methodsOfClass.get(className);
	}

	public Set<Method> getMethods(Interface interfaceName) {
		return methodsOfInterface.get(interfaceName);
	}
	
	public Method getVirtualMethod(Clazz className, String signature) {
		List<Clazz> superClasses = getSuperClassHierachy(className);
		for (Clazz clazz : superClasses) {
			Method method = getMethod(clazz, signature);
			if (method != null) {
				return method;
			}
		}
		return null;
	}
	
	public Method getVirtualMethod(final Interface interfaceName, final String signature) {
		InterfaceMethodFinder methodFinder = new InterfaceMethodFinder(this, signature);
		superInterface.traverseBreadthFirst(interfaceName, methodFinder, false);
		return methodFinder.getMethodFound();
	}
	
	public Set<Method> getVirtualMethods(Clazz className) {
		return getSuperClassHierachy(className).stream()
				.map(clazz -> methodsOfClass.get(clazz))
				.filter(methods -> methods != null)
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}

	public Set<Method> getVirtualMethods(Interface interfaceName) {
		Set<Interface> interfaces = getSuperInterfaces(interfaceName);
		interfaces.add(interfaceName);
		return interfaces.stream()
				.map(anInterface -> methodsOfInterface.get(anInterface))
				.filter(methods -> methods != null)
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}
}
