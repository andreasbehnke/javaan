package org.javaan;

import java.util.Set;

public class ClassContext {
	
	private final Graph<String> superClasses = new SimpleGraph<String>();
	
	public void addClass(String className) {
		superClasses.addNode(className);
	}
	
	public Set<String> getClasses() {
		return superClasses.getNodes();
	}
}
