package org.javaan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassContext {
	
	public final static String OBJECT_CLASS = Object.class.getCanonicalName();
	
	private final Map<String, String> superClasses = new HashMap<String, String>();
	
	public void addClass(String className) {
		if (className.equals(OBJECT_CLASS)) {
			superClasses.put(OBJECT_CLASS, null);
		} else {
			superClasses.put(className, OBJECT_CLASS);
		}
	}

	public void addSuperClass(String className, String superClassName) {
		if (!superClasses.containsKey(superClassName)) {
			addClass(superClassName);
		}
		superClasses.put(className, superClassName);
	}
	
	public boolean containsClass(String className) {
		return superClasses.containsKey(className);
	}
	
	public Set<String> getClasses() {
		return superClasses.keySet();
	}

	public String getSuperClass(String className) {
		return superClasses.get(className);
	}
	
	public List<String> getSuperClasses(String className) {
		List<String> classes = new ArrayList<String>();
		String currentClass = className;
		while(currentClass != null) {
			classes.add(currentClass);
			currentClass = superClasses.get(currentClass);
		}
		return classes;
	}
}
