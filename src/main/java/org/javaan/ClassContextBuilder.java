package org.javaan;

import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.javaan.model.ClassContext;
import org.javaan.model.ClassData;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private final List<ClassData> classes;
	
	public ClassContextBuilder(List<ClassData> classes) {
		this.classes = classes;
	}
	
	private void addInterface(ClassContext context, JavaClass javaClass) {
		Interface interfaceName = Interface.get(javaClass.getClassName());
		String[] interfaces = javaClass.getInterfaceNames();
		context.addInterface(interfaceName);
		for (String superInterfaceName : interfaces) {
			context.addSuperInterface(interfaceName, Interface.get(superInterfaceName));
		}
		for (Method method : javaClass.getMethods()) {
			context.addMethod(interfaceName, method.toString());
		}
	}
	
	private void addClass(ClassContext context, JavaClass javaClass) {
		Clazz className = Clazz.get(javaClass.getClassName());
		String superClassName = javaClass.getSuperclassName();
		if (superClassName == null || "java.lang.Object".equals(superClassName)) {
			context.addClass(className);
		} else {
			context.addSuperClass(className, Clazz.get(superClassName));
		}
		String[] interfaceNames = javaClass.getInterfaceNames();
		if (interfaceNames != null) {
			for (String interfaceName : interfaceNames) {
				Interface interfaze = Interface.get(interfaceName);
				context.addInterface(interfaze);
				context.addInterfaceOfClass(className, interfaze);
			}
		}
		for (Method method : javaClass.getMethods()) {
			context.addMethod(className, method.toString());
		}
	}
	
	private void addJavaClass(ClassContext context, JavaClass javaClass) {
		if (javaClass.isInterface()) {
			addInterface(context, javaClass);
		} else if (javaClass.isClass()) {
			addClass(context, javaClass);
		} else {
			throw new IllegalArgumentException("JavaClass is neither class nor interface");
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
