package org.javaan;

import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.javaan.model.ClassContext;
import org.javaan.model.ClassData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private final List<ClassData> classes;
	
	public ClassContextBuilder(List<ClassData> classes) {
		this.classes = classes;
	}
	
	private void addInterface(ClassContext context, JavaClass clazz) {
		String interfaceName = clazz.getClassName();
		String[] interfaces = clazz.getInterfaceNames();
		context.addInterface(interfaceName);
		for (String superInterfaceName : interfaces) {
			context.addSuperInterface(interfaceName, superInterfaceName);
		}
	}
	
	private void addClass(ClassContext context, JavaClass clazz) {
		String className = clazz.getClassName();
		String superClassName = clazz.getSuperclassName();
		if (superClassName == null || "java.lang.Object".equals(superClassName)) {
			context.addClass(className);
		} else {
			context.addSuperClass(className, superClassName);
		}
		String[] interfaceNames = clazz.getInterfaceNames();
		if (interfaceNames != null) {
			for (String interfaceName : interfaceNames) {
				context.addInterface(interfaceName);
				context.addInterfaceOfClass(className, interfaceName);
			}
		}
	}
	
	private void addJavaClass(ClassContext context, JavaClass clazz) {
		if (clazz.isInterface()) {
			addInterface(context, clazz);
		} else if (clazz.isClass()) {
			addClass(context, clazz);
		} else {
			throw new IllegalArgumentException("JavaClass is neither class nor interface");
		}
		String className = clazz.getClassName();
		for (Method method : clazz.getMethods()) {
			String methodName = method.toString();
			context.addMethod(className, methodName);
		}
	}
	
	public ClassContext build() {
		LOG.info("Creating class context ...");
		ClassContext context = new ClassContext();
		for (ClassData data : classes) {
			JavaClass clazz = data.getJavaClass();
			addJavaClass(context, clazz);
		}
		LOG.info("Created class context with {} classes and {} interfaces", context.getClasses().size(), context.getInterfaces().size());
		return context;
	}
}
