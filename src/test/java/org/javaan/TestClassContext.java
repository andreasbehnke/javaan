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
	public void testAddInterface() {
		ClassContext context = new ClassContext();
		context.addInterface("a");
		context.addInterface("b");
		context.addInterface("c");
		Set<String> interfaces = context.getInterfaces();
		assertNotNull(interfaces);
		assertEquals(3, interfaces.size());
		assertTrue(interfaces.contains("a"));
		assertTrue(interfaces.contains("b"));
		assertTrue(interfaces.contains("c"));
	}

	@Test
	public void testAddSuperInterfaces() {
		ClassContext context = new ClassContext();
		context.addInterface("a");
		context.addSuperInterface("b", "a");
		context.addSuperInterface("x", "y");
		
		assertTrue(context.containsInterface("y"));
		assertEquals(null, context.getSuperInterface("a"));
		assertEquals("a", context.getSuperInterface("b"));
		assertEquals(null, context.getSuperInterface("y"));
		assertEquals("y", context.getSuperInterface("x"));		
	}
	
	@Test
	public void testGetSuperInterfaces() {
		ClassContext context = new ClassContext();
		context.addInterface("a");
		context.addSuperInterface("b", "a");
		context.addSuperInterface("c", "b");
		
		List<String> superInterfaces = context.getSuperInterfaces("c");
		assertNotNull(superInterfaces);
		assertEquals(3, superInterfaces.size());
		assertEquals("c", superInterfaces.get(0));
		assertEquals("b", superInterfaces.get(1));
		assertEquals("a", superInterfaces.get(2));
	}
}
