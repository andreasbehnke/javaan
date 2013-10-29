package org.javaan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;

public class TestCallGraph {
	
	private final static Method METHODA = Method.get(Clazz.get("classa"), "methoda");

	private final static Method METHODB = Method.get(Clazz.get("classa"), "methodb");
	
	private final static Method METHODC = Method.get(Clazz.get("classa"), "methodc");

	private final static Method METHODD = Method.get(Clazz.get("classb"), "methodd");
	
	private final static Method METHODE = Method.get(Clazz.get("classb"), "methodE");
	
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
		MethodVisitor visitor = mock(MethodVisitor.class);
		InOrder order = inOrder(visitor);
		
		callGraph.traverseCallers(METHODE, -1, visitor);
		order.verify(visitor).visit(METHODE, 0);
		order.verify(visitor).visit(METHODD, 1);
		order.verify(visitor).visit(METHODC, 2);
		order.verify(visitor).visit(METHODA, 3);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	public void testTraverseCallees() {
		CallGraph callGraph = new CallGraph();
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		MethodVisitor visitor = mock(MethodVisitor.class);
		InOrder order = inOrder(visitor);
		
		callGraph.traverseCallees(METHODA, -1, visitor);
		order.verify(visitor).visit(METHODA, 0);
		order.verify(visitor).visit(METHODB, 1);
		order.verify(visitor).visit(METHODC, 1);
		order.verify(visitor).visit(METHODD, 2);
		order.verify(visitor).visit(METHODE, 3);
		order.verifyNoMoreInteractions();
	}
}