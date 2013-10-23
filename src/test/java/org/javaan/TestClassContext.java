package org.javaan;

import static org.junit.Assert.*;

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
		
		List<String> superClasses = context.getSuperClassHierachy("c");
		assertNotNull(superClasses);
		assertEquals(3, superClasses.size());
		assertEquals("c", superClasses.get(0));
		assertEquals("b", superClasses.get(1));
		assertEquals("a", superClasses.get(2));
	}
	
	@Test
	public void testGetSpecializationOfClass() {
		ClassContext context = new ClassContext();
		context.addClass("a");
		context.addSuperClass("b", "a");
		context.addSuperClass("c", "b");

		Set<String> spec = context.getSpecializationsOfClass("a");
		assertNotNull(spec);
		assertEquals(2, spec.size());
		assertTrue(spec.contains("b"));
		assertTrue(spec.contains("c"));
		
		spec = context.getSpecializationsOfClass("c");
		assertNotNull(spec);
		assertEquals(0, spec.size());
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
	public void testGetSuperInterfaces() {
		ClassContext context = new ClassContext();
		context.addInterface("a");
		context.addSuperInterface("b", "a");
		context.addSuperInterface("b", "c");
		context.addSuperInterface("c", "d");
		context.addSuperInterface("x", "y");
		
		Set<String> interfaces = context.getSuperInterfaces("a");
		assertNotNull(interfaces);
		assertEquals(0, interfaces.size());
		
		interfaces = context.getSuperInterfaces("b");
		assertNotNull(interfaces);
		assertEquals(3, interfaces.size());
		assertTrue(interfaces.contains("a"));
		assertTrue(interfaces.contains("c"));
		assertTrue(interfaces.contains("d"));
		
		assertTrue(context.containsInterface("y"));
		interfaces = context.getSuperInterfaces("x");
		assertNotNull(interfaces);
		assertEquals(1, interfaces.size());
		assertTrue(interfaces.contains("y"));
	}
	
	@Test
	public void testSpecializationOfInterface() {
		ClassContext context = new ClassContext();
		context.addInterface("a");
		context.addSuperInterface("b", "a");
		context.addSuperInterface("c", "b");
		
		Set<String> interfaces = context.getSpecializationOfInterface("a");
		assertNotNull(interfaces);
		assertEquals(2, interfaces.size());
		assertTrue(interfaces.contains("b"));
		assertTrue(interfaces.contains("c"));
		
		interfaces = context.getSpecializationOfInterface("c");
		assertNotNull(interfaces);
		assertEquals(0, interfaces.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddInterfaceOfClassUnknownClass() {
		ClassContext context = new ClassContext();
		context.addInterface("ia");
		context.addInterfaceOfClass("classa", "ia");
		fail("Exepect Exception");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddInterfaceOfClassUnknownInterface() {
		ClassContext context = new ClassContext();
		context.addClass("classa");
		context.addInterfaceOfClass("classa", "ia");
		fail("Exepect Exception");
	}
	
	@Test
	public void testGetInterfacesOfClass() {
		ClassContext context = new ClassContext();
		context.addSuperInterface("ia", "ib");
		context.addSuperInterface("ib", "ic");
		context.addSuperInterface("ix", "iy");
		
		context.addSuperClass("classa", "classb");
		context.addSuperClass("classb", "classc");
		context.addSuperClass("classc", "classd");
		
		context.addInterfaceOfClass("classa", "ia");
		context.addInterfaceOfClass("classc", "ix");
		
		Set<String> interfaces = context.getInterfacesOfClass("classd");
		assertNotNull(interfaces);
		assertEquals(0, interfaces.size());
		
		interfaces = context.getInterfacesOfClass("classa");
		assertNotNull(interfaces);
		assertEquals(5, interfaces.size());
		assertTrue(interfaces.contains("ia"));
		assertTrue(interfaces.contains("ib"));
		assertTrue(interfaces.contains("ic"));
		assertTrue(interfaces.contains("ix"));
		assertTrue(interfaces.contains("iy"));
	}
	
	@Test
	public void testGetImplementations() {
		/*
			interfaces:
			- ia --> ib --> ix
			- ic
			
			classes:
			classa
			classd --> classc --> classb
			 
			direct implementations:
			classa --> ia, ic
			classb --> ia
			
		 */
		ClassContext context = new ClassContext();
		context.addInterface("ia");
		context.addSuperInterface("ia", "ib");
		context.addSuperInterface("ib", "ix");
		context.addInterface("ic");
		context.addClass("classa");
		context.addClass("classb");
		context.addSuperClass("classc", "classb");
		context.addSuperClass("classd", "classc");
		context.addInterfaceOfClass("classa", "ia");
		context.addInterfaceOfClass("classa", "ic");
		context.addInterfaceOfClass("classb", "ia");	
		
		Set<String> classes = context.getImplementations("ia");
		assertNotNull(classes);
		assertEquals(4, classes.size());
		assertTrue(classes.contains("classa"));
		assertTrue(classes.contains("classb"));
		assertTrue(classes.contains("classc"));
		assertTrue(classes.contains("classd"));
		
		classes = context.getImplementations("ib");
		assertNotNull(classes);
		assertEquals(4, classes.size());
		assertTrue(classes.contains("classa"));
		assertTrue(classes.contains("classb"));
		assertTrue(classes.contains("classc"));
		assertTrue(classes.contains("classd"));
		
		classes = context.getImplementations("ic");
		assertNotNull(classes);
		assertEquals(1, classes.size());
		assertTrue(classes.contains("classa"));
		
		classes = context.getImplementations("ix");
		assertNotNull(classes);
		assertEquals(4, classes.size());
		assertTrue(classes.contains("classa"));
		assertTrue(classes.contains("classb"));
		assertTrue(classes.contains("classc"));
		assertTrue(classes.contains("classd"));
	}
}
