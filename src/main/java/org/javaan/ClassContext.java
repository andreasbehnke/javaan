package org.javaan;

import java.util.List;
import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.DigraphImpl;
import org.javaan.graph.SingleChildGraph;
import org.javaan.graph.SingleChildGraphImpl;

public class ClassContext {
	
	private final SingleChildGraph<String> superClass = new SingleChildGraphImpl<String>();

	private final Digraph<String> superInterface = new DigraphImpl<String>();

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
}
