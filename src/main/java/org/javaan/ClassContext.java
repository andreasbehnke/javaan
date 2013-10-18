package org.javaan;

import java.util.List;
import java.util.Set;

import org.javaan.graph.SingleChildGraph;
import org.javaan.graph.SingleChildGraphImpl;

public class ClassContext {
	
	private final SingleChildGraph<String> superClass = new SingleChildGraphImpl<String>();

	private final SingleChildGraph<String> superInterface = new SingleChildGraphImpl<String>();

	public void addClass(String className) {
		superClass.addNode(className);
	}

	public void addSuperClass(String className, String superClassName) {
		superClass.addEdge(className, superClassName);
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
	
	public List<String> getSuperClasses(String className) {
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

	public String getSuperInterface(String interfaceName) {
		return superInterface.getChild(interfaceName);
	}
	
	public List<String> getSuperInterfaces(String interfaceName) {
		return superInterface.getPath(interfaceName);
	}
}
