package org.javaan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.DigraphImpl;
import org.javaan.graph.SingleChildGraph;
import org.javaan.graph.SingleChildGraphImpl;

public class ClassContext {
	
	private final SingleChildGraph<String> superClass = new SingleChildGraphImpl<String>();

	private final Digraph<String> superInterface = new DigraphImpl<String>();
	
	private final Digraph<String> interfaceOfClass = new DigraphImpl<String>();

	public void addClass(String className) {
		superClass.addNode(className);
		interfaceOfClass.addNode(className);
	}

	public void addSuperClass(String className, String superClassName) {
		superClass.addEdge(className, superClassName);
		interfaceOfClass.addNode(className);
		interfaceOfClass.addNode(superClassName);
	}
	
	public boolean containsClass(String className) {
		return superClass.containsNode(className);
	}
	
	public Set<String> getClasses() {
		return superClass.getNodes();
	}

	public String getSuperClass(String className) {
		return superClass.getChild(className);
	}
	
	public List<String> getSuperClassHierachy(String className) {
		return superClass.getPath(className);
	}
	
	public void addInterface(String interfaceName) {
		superInterface.addNode(interfaceName);
	}

	public void addSuperInterface(String interfaceName, String superInterfaceName) {
		superInterface.addEdge(interfaceName, superInterfaceName);
	}
	
	public boolean containsInterface(String interfaceName) {
		return superInterface.containsNode(interfaceName);
	}
	
	public Set<String> getInterfaces() {
		return superInterface.getNodes();
	}

	public Set<String> getSuperInterfaces(String interfaceName) {
		return superInterface.getSuccessors(interfaceName);
	}
	
	public void addInterfaceOfClass(String className, String interfaceName) {
		if (!superInterface.containsNode(interfaceName)) {
			throw new IllegalArgumentException("Unknown interface " + interfaceName);
		}
		if (!superClass.containsNode(className)) {
			throw new IllegalArgumentException("Unknown class " + className);
		}
		interfaceOfClass.addEdge(className, interfaceName);
	}
	
	private Set<String> getDirectIntefacesOfClass(String className) {
		Set<String> childs = interfaceOfClass.getChilds(className);
		Set<String> interfaces = new HashSet<String>(childs);
		for (String interfaceName : childs) {
			interfaces.addAll(superInterface.getSuccessors(interfaceName));
		}
		return interfaces;
	}
	
	public Set<String> getInterfacesOfClass(String className) {
		List<String> superClasses = superClass.getPath(className);
		Set<String> interfaces = new HashSet<String>();
		for (String superClassName : superClasses) {
			interfaces.addAll(getDirectIntefacesOfClass(superClassName));
		}
		return interfaces;
	}
	
	public Set<String> getImplementations(String interfaceName) {
		Set<String> implementingClasses = new HashSet<String>();
		Set<String> interfaces = superInterface.getPredecessors(interfaceName);
		interfaces.add(interfaceName);
		Set<String> classes = new HashSet<String>();
		// find direct implementations of all specialized interfaces
		for (String specializedInterface : interfaces) {
			classes.addAll(interfaceOfClass.getParents(specializedInterface));
		}
		// find all specializations of implementations 
		for (String className : classes) {
			implementingClasses.add(className);
			implementingClasses.addAll(superClass.getPredecessors(className));
		}
		return implementingClasses;
	}
}
