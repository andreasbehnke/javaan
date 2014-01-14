package org.javaan.model;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.graph.VertexEdgeGraphVisitor;
import org.junit.Test;

public class TestCallGraph {
	
	private static final Clazz A = new Clazz("classa");

	private static final Clazz B = new Clazz("classb");

	private static final Clazz C = new Clazz("classc");

	private static final Clazz D = new Clazz("classd");

	private final static Method METHODA = new Method(A, "methoda");

	private final static Method METHODB = new Method(A, "methodb");

	private final static Method METHODC = new Method(A, "methodc");

	private final static Method METHODD = new Method(B, "methodd");

	private final static Method METHODD1 = new Method(B, "methodd1");

	private final static Method METHODE = new Method(C, "methode");

	private final static Method METHODF = new Method(D, "methodf");
	
	private ClassContext createClassContext() {
		ClassContext classContext = new ClassContext();
		classContext.addClass(A);
		classContext.addClass(B);
		classContext.addSuperClass(C, D);
		return classContext;
	}

	@Test
	public void testGetCallers() {
		CallGraph callGraph = new CallGraph(createClassContext());
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
	public void testMultipleUsageOfType() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODD);
		callGraph.addCall(METHODA, METHODD1);
		
		Set<Method> outgoingEdgesOfA = callGraph.getUsageOfClassGraph().outgoingEdgesOf(A);
		assertNotNull(outgoingEdgesOfA);
		assertEquals(2, outgoingEdgesOfA.size());
		assertTrue(outgoingEdgesOfA.contains(METHODD));
		assertTrue(outgoingEdgesOfA.contains(METHODD1));
	}

	@Test
	public void testGetCallees() {
		CallGraph callGraph = new CallGraph(createClassContext());
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
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		VertexEdgeGraphVisitor<Method> visitor = mock(VertexEdgeGraphVisitor.class);
		
		callGraph.traverseCallers(METHODE, visitor);
		verify(visitor, times(4)).finished();
		verify(visitor).visitVertex(METHODE, 0);
		verify(visitor).visitVertex(METHODD, 1);
		verify(visitor).visitVertex(METHODC, 2);
		verify(visitor).visitVertex(METHODA, 3);
		//verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testTraverseCallees() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		VertexEdgeGraphVisitor<Method> visitor = mock(VertexEdgeGraphVisitor.class);

		callGraph.traverseCallees(METHODA, visitor);
		verify(visitor, times(5)).finished();
		verify(visitor).visitVertex(METHODA, 0);
		verify(visitor).visitVertex(METHODB, 1);
		verify(visitor).visitVertex(METHODC, 1);
		verify(visitor).visitVertex(METHODD, 2);
		verify(visitor).visitVertex(METHODE, 3);
		//verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testGetLeafCallers() {
		CallGraph callGraph = new CallGraph(createClassContext());
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
		CallGraph callGraph = new CallGraph(createClassContext());
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
	
	@Test
	public void testTraverseUsedTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD); 
		callGraph.addCall(METHODD, METHODE);
		GraphVisitor<Clazz, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsedTypes(A, visitor);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(A, 0);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitVertex(C, 2);
		//verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testTraverseUsingTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		GraphVisitor<Clazz, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsingTypes(C, visitor);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(C, 0);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitVertex(A, 2);
		//verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testGetLeafUsedTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		
		Set<Clazz> leaves = callGraph.getLeafUsedTypes(A);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(C));
	}
	
	@Test
	public void testGetLeafUsingTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB); // A --> A
		callGraph.addCall(METHODA, METHODC); // A --> A
		callGraph.addCall(METHODC, METHODD); // A --> B
		callGraph.addCall(METHODD, METHODE); // B --> C
		
		Set<Clazz> leaves = callGraph.getLeafUsingTypes(C);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(A));
	}
	
	@Test
	public void testGetDependencyCycles() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(METHODA, METHODB);
		callGraph.addCall(METHODA, METHODC);
		callGraph.addCall(METHODC, METHODD);
		callGraph.addCall(METHODD, METHODE);
		callGraph.addCall(METHODE, METHODF);
		callGraph.addCall(METHODF, METHODE);// first cycle C --> D --> C: This must be ignored, because D inherits C!
		callGraph.addCall(METHODD, METHODA);// second cylce A --> B --> A

		List<Set<Clazz>> cycles = callGraph.getDependencyCycles();
		assertNotNull(cycles);
		assertEquals(1, cycles.size());
		Set<Clazz> setABA = cycles.get(0);

		assertEquals(2, setABA.size());
		assertTrue(setABA.contains(A));
		assertTrue(setABA.contains(B));
	}
}