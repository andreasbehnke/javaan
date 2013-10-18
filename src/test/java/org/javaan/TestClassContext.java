package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestClassContext {
	
	@Test
	public void testAddClass() {
		ClassContext context = new ClassContext();
		context.addClass("a");
		context.addClass("b");
		context.addClass("c");
		Set<String> classes = context.getClasses();
		assertNotNull(classes);
		assertEquals(3, classes.size());
		assertTrue(classes.contains("a"));
		assertTrue(classes.contains("b"));
		assertTrue(classes.contains("c"));
	}

	@Test
	public void testAddSuperClass() {
		ClassContext context = new ClassContext();
		context.addClass("a");
		context.addSuperClass("b", "a");
		context.addSuperClass("x", "y");
		
		assertTrue(context.containsClass("y"));
		assertEquals(null, context.getSuperClass("a"));
		assertEquals("a", context.getSuperClass("b"));
		assertEquals(null, context.getSuperClass("y"));
		assertEquals("y", context.getSuperClass("x"));		
	}
	
	@Test
	public void testGetSuperClasses() {
		ClassContext context = new ClassContext();
		context.addClass("a");
		context.addSuperClass("b", "a");
		context.addSuperClass("c", "b");
		
		List<String> superClasses = context.getSuperClasses("c");
		assertNotNull(superClasses);
		assertEquals(3, superClasses.size());
		assertEquals("c", superClasses.get(0));
		assertEquals("b", superClasses.get(1));
		assertEquals("a", superClasses.get(2));
	}
	
	@Test
	public void testGetInterfacesOfClass() {
		ClassContext context = new ClassContext();
		context.addClass("interfaceC");
		context.addClass("interfaceA");
		context.addSuperClass("interfaceB", "interfaceA");
		context.addClass("a");
		context.addClass("b");
		context.addInterface("a", "interfaceB");
		context.addInterface("b", "interfaceA");
		context.addInterface("b", "interfaceC");
		
		Set<String> interfaces = context.getInterfacesOfClass("a");
		assertNotNull(interfaces);
		assertEquals(2, interfaces.size());
		assertTrue(interfaces.contains("interfaceA"));
		assertTrue(interfaces.contains("interfaceB"));
		interfaces = context.getInterfacesOfClass("b");
		assertNotNull(interfaces);
		assertEquals(2, interfaces.size());
		assertTrue(interfaces.contains("interfaceA"));
		assertTrue(interfaces.contains("interfaceC"));
	}
}
