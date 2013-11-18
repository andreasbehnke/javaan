package org.javaan.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javaan.graph.NamedObjectDirectedGraph;
import org.javaan.graph.ParentChildGraph;
import org.javaan.graph.ParentChildGraphImpl;
import org.javaan.graph.SingleChildGraph;
import org.javaan.graph.SingleChildGraphImpl;

public class ClassContext {
	
	private final SingleChildGraph<Clazz> superClass = new SingleChildGraphImpl<Clazz>();

	private final NamedObjectDirectedGraph<Interface> superInterface = new NamedObjectDirectedGraph<Interface>();
	
	private final ParentChildGraph<Clazz, Interface> interfaceOfClass = new ParentChildGraphImpl<Clazz, Interface>();
	
	private final ParentChildGraph<Clazz, Method> methodsOfClass = new ParentChildGraphImpl<Clazz, Method>();
	
	private final ParentChildGraph<Interface, Method> methodsOfInterface = new ParentChildGraphImpl<Interface, Method>();

	public void addClass(Clazz className) {
		if (className == null) {
			throw new IllegalArgumentException("Parameter className must not be null");
		}
		superClass.addNode(className);
		interfaceOfClass.addParent(className);
	}

	public void addSuperClass(Clazz className, Clazz superClassName) {
		if (className == null) {
			throw new IllegalArgumentException("Parameter className must not be null");
		}
		if (superClassName == null) {
			throw new IllegalArgumentException("Parameter superClassName must not be null");
		}
		superClass.addEdge(className, superClassName);
		interfaceOfClass.addParent(className);
		interfaceOfClass.addParent(superClassName);
	}
	
	public boolean containsClass(Clazz className) {
		return superClass.containsNode(className);
	}
	
	public Set<Clazz> getClasses() {
		return superClass.getNodes();
	}

	public Clazz getSuperClass(Clazz className) {
		return superClass.getChild(className);
	}
	
	public List<Clazz> getSuperClassHierachy(Clazz className) {
		return superClass.getPath(className);
	}
	
	public Set<Clazz> getSpecializationsOfClass(Clazz className) {
		return superClass.getPredecessors(className);
	}
	
	public void addInterface(Interface interfaceName) {
		superInterface.addVertex(interfaceName);
	}

	public void addSuperInterface(Interface interfaceName, Interface superInterfaceName) {
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
		if (!superClass.containsNode(className)) {
			throw new IllegalArgumentException("Unknown class " + className);
		}
		interfaceOfClass.addEdge(className, interfaceName);
	}
	
	private Set<Interface> getDirectIntefacesOfClass(Clazz className) {
		Set<Interface> childs = interfaceOfClass.getChilds(className);
		Set<Interface> interfaces = new HashSet<Interface>(childs);
		for (Interface interfaceName : childs) {
			interfaces.addAll(superInterface.successorsOf(interfaceName));
		}
		return interfaces;
	}
	
	public Set<Interface> getInterfacesOfClass(Clazz className) {
		List<Clazz> superClasses = superClass.getPath(className);
		Set<Interface> interfaces = new HashSet<Interface>();
		for (Clazz superClassName : superClasses) {
			interfaces.addAll(getDirectIntefacesOfClass(superClassName));
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
			classes.addAll(interfaceOfClass.getParents(specializedInterface));
		}
		// find all specializations of implementations
		for (Clazz className : classes) {
			implementingClasses.add(className);
			implementingClasses.addAll(superClass.getPredecessors(className));
		}
		return implementingClasses;
	}
	
	public void addMethod(Method method) {
		Type typeName = method.getType();
		switch (typeName.getJavaType()) {
		case CLASS:
			if (!superClass.containsNode((Clazz)typeName)) {
				throw new IllegalArgumentException("Unknown class " + typeName);
			}
			methodsOfClass.addEdge((Clazz)typeName, method);
			break;
		case INTERFACE:
			if (!superInterface.containsVertex((Interface)typeName)) {
				throw new IllegalArgumentException("Unknown interface " + typeName);
			}
			methodsOfInterface.addEdge((Interface)typeName, method);
			break;
		default:
			break;
		}
	}

	private Method findMethod(Set<Method> methods, String signature) {
		for (Method method : methods) {
			if (method.getSignature().equals(signature)) {
				return method;
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
		Set<Method> methods = new HashSet<Method>();
		methods.addAll(methodsOfClass.getChilds());
		methods.addAll(methodsOfInterface.getChilds());
		return methods;
	}
	
	public Set<Method> getMethods(Clazz className) {
		Set<Method> methods = methodsOfClass.getChilds(className);
		if (methods == null) {
			methods = new HashSet<Method>();
		}
		return methods;
	}

	public Set<Method> getMethods(Interface interfaceName) {
		Set<Method> methods = methodsOfInterface.getChilds(interfaceName);
		if (methods == null) {
			methods = new HashSet<Method>();
		}
		return methods;
	}
	
	public Method getVirtualMethod(Clazz className, String signature) {
		return findMethod(getVirtualMethods(className), signature);
	}
	
	public Method getVirtualMethod(Interface interfaceName, String signature) {
		return findMethod(getVirtualMethods(interfaceName), signature);
	}
	
	public Set<Method> getVirtualMethods(Clazz className) {
		List<Clazz> superClasses = getSuperClassHierachy(className);
		Set<Method> methods = new HashSet<Method>();
		for (Clazz clazz : superClasses) {
			methods.addAll(methodsOfClass.getChilds(clazz));
		}
		return methods;
	}

	public Set<Method> getVirtualMethods(Interface interfaceName) {
		Set<Interface> superInterfaces = getSuperInterfaces(interfaceName);
		superInterfaces.add(interfaceName);
		Set<Method> methods = new HashSet<Method>();
		for (Interface interfaze : superInterfaces) {
			methods.addAll(methodsOfInterface.getChilds(interfaze));
		}
		return methods;
	}
}
