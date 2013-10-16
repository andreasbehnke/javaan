package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

	
}
