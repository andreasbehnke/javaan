package org.javaan;

import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private final List<ClassData> classes;
	
	public ClassContextBuilder(List<ClassData> classes) {
		this.classes = classes;
	}
	
	public ClassContext build() {
		LOG.info("Creating class context ...");
		ClassContext context = new ClassContext();
		for (ClassData data : classes) {
			JavaClass clazz = data.getJavaClass();
			String className = clazz.getClassName();
			String superClassName = clazz.getSuperclassName();
			if (superClassName == null || "java.lang.Object".equals(superClassName)) {
				context.addClass(className);
			} else {
				context.addSuperClass(className, superClassName);
			}
		}
		LOG.info("Created class context with {} classes", context.getClasses().size());
		return context;
	}
}