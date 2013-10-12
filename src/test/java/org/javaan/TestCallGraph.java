package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class TestCallGraph {
	
	@Test
	public void testAdd() {
		CallGraph callGraph = new CallGraph();
		callGraph.add("a");
		
		assertTrue(callGraph.containsMethod("a"));
	}
	
	@Test
	public void testAddCaller() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCaller("x", "a");
		
		assertTrue(callGraph.containsMethod("a"));
		assertTrue(callGraph.containsMethod("x"));
		Set<String> caller = callGraph.getCallers("a");
		assertNotNull(caller);
		assertEquals(0, caller.size());
		caller = callGraph.getCallers("x");
		assertNotNull(caller);
		assertEquals(1, caller.size());
		assertTrue(caller.contains("a"));
	}

	@Test
	public void testGetCallers() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCaller("x", "a");
		callGraph.addCaller("x", "b");
		callGraph.addCaller("x", "c");
		
		Set<String> callers = callGraph.getCallers("x");
		assertNotNull(callers);
		assertTrue(callers.contains("a"));
		assertTrue(callers.contains("b"));
		assertTrue(callers.contains("c"));
	}
	
	@Test
	public void testHasCallers() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCaller("x", "a");
		
		assertTrue(callGraph.hasCallers("x"));
		assertFalse(callGraph.hasCallers("a"));
	}
	
	@Test
	public void testContainsMethod() {
		CallGraph callGraph = new CallGraph();
		callGraph.add("x");
		
		assertTrue(callGraph.containsMethod("x"));
	}
	
	@Test
	public void testGetCallingEntryMethods() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCaller("x", "a");
		callGraph.addCaller("x", "b");
		callGraph.addCaller("x", "c");
		callGraph.addCaller("c", "d");
		callGraph.addCaller("c", "e");
		callGraph.addCaller("e", "f");
		
		Set<String> callers = callGraph.getCallingEntryMethods("x");
		assertNotNull(callers);
		assertEquals(4, callers.size());
		assertTrue(callers.contains("a"));
		assertTrue(callers.contains("b"));
		assertTrue(callers.contains("d"));
		assertTrue(callers.contains("f"));
		callers = callGraph.getCallingEntryMethods("c");
		assertNotNull(callers);
		assertEquals(2, callers.size());
		assertTrue(callers.contains("d"));
		assertTrue(callers.contains("f"));
		callers = callGraph.getCallingEntryMethods("a");
		assertNotNull(callers);
		assertEquals(0, callers.size());
		callers = callGraph.getCallingEntryMethods("b");
		assertNotNull(callers);
		assertEquals(0, callers.size());
		callers = callGraph.getCallingEntryMethods("e");
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains("f"));
	}
	
	@Test
	public void testGetCallingEntryMethodsCycle() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCaller("x", "a");
		callGraph.addCaller("a", "b");
		callGraph.addCaller("b", "x");
		callGraph.addCaller("a", "c");
		
		Set<String> callers = callGraph.getCallingEntryMethods("x");
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains("c"));
	}
}
