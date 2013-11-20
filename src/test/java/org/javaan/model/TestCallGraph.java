package org.javaan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Set;

import org.javaan.graph.NamedObjectVisitor;
import org.junit.Test;

public class TestCallGraph {
	
	private final static Method METHODA = new Method(new Clazz("classa"), null, "methoda");

	private final static Method METHODB = new Method(new Clazz("classa"), null, "methodb");
	
	private final static Method METHODC = new Method(new Clazz("classa"), null, "methodc");

	private final static Method METHODD = new Method(new Clazz("classb"), null, "methodd");
	
	private final static Method METHODE = new Method(new Clazz("classb"), null, "methodE");
	
	@Test
	public void testGetCallers() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		
		Set<Method> callers = callGraph.getCallers(METHODA);
		assertNotNull(callers);
		assertEquals(0, callers.size());
		callers = callGraph.getCallers(METHODB);
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains(METHODA));
		callers = callGraph.getCallers(METHODC);
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains(METHODA));
	}

	@Test
	public void testGetCallees() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		
		Set<Method> callees = callGraph.getCallees(METHODA);
		assertNotNull(callees);
		assertEquals(2, callees.size());
		assertTrue(callees.contains(METHODB));
		assertTrue(callees.contains(METHODC));
		callees = callGraph.getCallees(METHODB);
		assertNotNull(callees);
		assertEquals(0, callees.size());
		callees = callGraph.getCallees(METHODC);
		assertNotNull(callees);
		assertEquals(0, callees.size());
	}

	@Test
	public void testTraverseCallers() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		NamedObjectVisitor<Method> visitor = mock(NamedObjectVisitor.class);
		
		callGraph.traverseCallers(METHODE, visitor);
		verify(visitor).visit(METHODE, 0);
		verify(visitor).visit(METHODD, 1);
		verify(visitor).visit(METHODC, 2);
		verify(visitor).visit(METHODA, 3);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testTraverseCallees() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		NamedObjectVisitor<Method> visitor = mock(NamedObjectVisitor.class);

		callGraph.traverseCallees(METHODA, visitor);
		verify(visitor).visit(METHODA, 0);
		verify(visitor).visit(METHODB, 1);
		verify(visitor).visit(METHODC, 1);
		verify(visitor).visit(METHODD, 2);
		verify(visitor).visit(METHODE, 3);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testGetLeafCallers() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		
		Set<Method> leafCallers = callGraph.getLeafCallers(METHODA);
		assertNotNull(leafCallers);
		assertEquals(0, leafCallers.size());
		
		leafCallers = callGraph.getLeafCallers(METHODD);
		assertNotNull(leafCallers);
		assertEquals(1, leafCallers.size());
		assertTrue(leafCallers.contains(METHODA));
	}
	
	@Test
	public void testGetLeafCallees() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		
		Set<Method> leafCallees = callGraph.getLeafCallees(METHODA);
		assertNotNull(leafCallees);
		assertEquals(2, leafCallees.size());
		assertTrue(leafCallees.contains(METHODB));
		assertTrue(leafCallees.contains(METHODD));
		
		leafCallees = callGraph.getLeafCallees(METHODD);
		assertNotNull(leafCallees);
		assertEquals(0, leafCallees.size());
	}
}