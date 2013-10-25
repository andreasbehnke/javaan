package org.javaan;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.javaan.model.ClassContext;
import org.javaan.model.ClassData;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Method;
import org.junit.Test;

public class TestClassContextBuilder implements TestConstants {

	private List<ClassData> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
	}
	
	@Test
	public void testBuild() throws IOException {
		ClassContext context = new ClassContextBuilder(loadClasses()).build();
		
		Set<Clazz> classes = context.getClasses();
		assertNotNull(classes);
		assertEquals(NUMBER_OF_CLASSES, classes.size());
		assertEquals(SUPER_CLASS_OF_CLASS_C, context.getSuperClass(CLASS_C));
		
		Set<Interface> interfaces = context.getInterfaces();
		assertNotNull(interfaces);
		assertEquals(NUMBER_OF_INTERFACES, interfaces.size());
		
		interfaces = context.getSuperInterfaces(INTERFACE_C);
		assertNotNull(interfaces);
		assertEquals(2, interfaces.size());
		assertTrue(interfaces.contains(SUPER_INTERFACE1_OF_INTERFACE_C));
		assertTrue(interfaces.contains(SUPER_INTERFACE2_OF_INTERFACE_C));
		
		Set<Clazz> implementations = context.getImplementations(INTERFACE_C);
		assertNotNull(implementations);
		assertEquals(1, implementations.size());
		assertTrue(implementations.contains(CLASS_A));
		
		Set<Method> methods =  context.getMethods(CLASS_A);
		assertNotNull(methods);
		assertEquals(2, methods.size());
		assertTrue(methods.contains(Method.get(CLASS_A, "public String methodInterfaceB(String a, String b)")));
		
	}
}