package org.javaan;

import java.util.Set;

public class ClassContext {
	
	public final static String OBJECT_CLASS = Object.class.getCanonicalName();
	
	private final Graph<String> superClasses = new SingleChildGraph<String>();
	
	public void addClass(String className) {
		superClasses.addEdge(className, OBJECT_CLASS);
	}

	public void addSuperClass(String className, String superClassName) {
		if (!superClasses.containsNode(superClassName)) {
			superClasses.addEdge(superClassName, OBJECT_CLASS);
		}
		superClasses.addEdge(className, superClassName);
	}
	
	public boolean containsClass(String className) {
		return superClasses.containsNode(className);
	}
	
	public String getSuperClass(String className) {
		return superClasses.getChilds(className).iterator().next();
	}
	
	public Set<String> getClasses() {
		return superClasses.getNodes();
	}
}
