package org.javaan;

import java.util.List;

import org.apache.bcel.classfile.JavaClass;

public class ClassContextBuilder {

	private final List<JavaClass> classes;
	
	public ClassContextBuilder(List<JavaClass> classes) {
		this.classes = classes;
	}
	
	public ClassContext build() {
		ClassContext context = new ClassContext();
		for (JavaClass clazz : classes) {
			context.addSuperClass(clazz.getClassName(), clazz.getSuperclassName());
		}
		return context;
	}
}
