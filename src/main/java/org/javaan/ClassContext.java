package org.javaan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.DigraphImpl;

public class ClassContext {
	
	private final Map<String, String> superClass = new HashMap<String, String>();
	
	private final Digraph<String> interfaceOfClass = new DigraphImpl<String>();

	public void addClass(String className) {
		superClass.put(className, null);
	}

	public void addSuperClass(String className, String superClassName) {
		if (!superClass.containsKey(superClassName)) {
			addClass(superClassName);
		}
		superClass.put(className, superClassName);
	}
	
	public boolean containsClass(String className) {
		return superClass.containsKey(className);
	}
	
	public Set<String> getClasses() {
		return superClass.keySet();
	}

	public String getSuperClass(String className) {
		return superClass.get(className);
	}
	
	public List<String> getSuperClasses(String className) {
		List<String> classes = new ArrayList<String>();
		String currentClass = className;
		while(currentClass != null) {
			classes.add(currentClass);
			currentClass = superClass.get(currentClass);
		}
		return classes;
	}
	
	public void addInterface(String className, String interfaceName) {
		interfaceOfClass.addEdge(className, interfaceName);
	}
	
	public Set<String> getInterfacesOfClass(String className) {
		Set<String> interfaces = new HashSet<String>();
		Set<String> directInterfaces = interfaceOfClass.getChilds(className);
		for (String interfaze : directInterfaces) {
			interfaces.addAll(getSuperClasses(interfaze));
		}
		return interfaces;
	}
}
