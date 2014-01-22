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
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.graph.VertexEdgeGraphVisitor;
import org.junit.Test;

public class TestCallGraph {
	
	private static final Package ABC = new Package("a.b.c");
	
	private static final Package DEF = new Package("d.e.f");
	
	private static final Clazz A = new Clazz("a.b.c.classa");

	private static final Clazz B = new Clazz("d.e.f.classb");

	private static final Clazz C = new Clazz("a.b.c.classc");

	private static final Clazz D = new Clazz("d.e.f.classd");

	private final static Method A_METHODA = new Method(A, "methoda");

	private final static Method A_METHODB = new Method(A, "methodb");

	private final static Method A_METHODC = new Method(A, "methodc");

	private final static Method B_METHODD = new Method(B, "methodd");

	private final static Method B_METHODD1 = new Method(B, "methodd1");

	private final static Method C_METHODE = new Method(C, "methode");

	private final static Method D_METHODF = new Method(D, "methodf");
	
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
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		
		Set<Method> callers = callGraph.getCallers(A_METHODA);
		assertNotNull(callers);
		assertEquals(0, callers.size());
		callers = callGraph.getCallers(A_METHODB);
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains(A_METHODA));
		callers = callGraph.getCallers(A_METHODC);
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains(A_METHODA));
	}
	
	@Test
	public void testMultipleUsageOfType() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, B_METHODD);
		callGraph.addCall(A_METHODA, B_METHODD1);
		
		Set<Method> outgoingEdgesOfA = callGraph.getUsageOfClassGraph().outgoingEdgesOf(A);
		assertNotNull(outgoingEdgesOfA);
		assertEquals(2, outgoingEdgesOfA.size());
		assertTrue(outgoingEdgesOfA.contains(B_METHODD));
		assertTrue(outgoingEdgesOfA.contains(B_METHODD1));
	}

	@Test
	public void testGetCallees() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		
		Set<Method> callees = callGraph.getCallees(A_METHODA);
		assertNotNull(callees);
		assertEquals(2, callees.size());
		assertTrue(callees.contains(A_METHODB));
		assertTrue(callees.contains(A_METHODC));
		callees = callGraph.getCallees(A_METHODB);
		assertNotNull(callees);
		assertEquals(0, callees.size());
		callees = callGraph.getCallees(A_METHODC);
		assertNotNull(callees);
		assertEquals(0, callees.size());
	}

	@Test
	public void testTraverseCallers() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		callGraph.addCall(B_METHODD, C_METHODE);
		VertexEdgeGraphVisitor<Method> visitor = mock(VertexEdgeGraphVisitor.class);
		
		callGraph.traverseCallers(C_METHODE, visitor);
		verify(visitor, times(4)).finished();
		verify(visitor).visitVertex(C_METHODE, 0);
		verify(visitor).visitVertex(B_METHODD, 1);
		verify(visitor).visitVertex(A_METHODC, 2);
		verify(visitor).visitVertex(A_METHODA, 3);
	}
	
	@Test
	public void testTraverseCallees() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		callGraph.addCall(B_METHODD, C_METHODE);
		VertexEdgeGraphVisitor<Method> visitor = mock(VertexEdgeGraphVisitor.class);

		callGraph.traverseCallees(A_METHODA, visitor);
		verify(visitor, times(5)).finished();
		verify(visitor).visitVertex(A_METHODA, 0);
		verify(visitor).visitVertex(A_METHODB, 1);
		verify(visitor).visitVertex(A_METHODC, 1);
		verify(visitor).visitVertex(B_METHODD, 2);
		verify(visitor).visitVertex(C_METHODE, 3);
	}
	
	@Test
	public void testGetLeafCallers() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		
		Set<Method> leafCallers = callGraph.getLeafCallers(A_METHODA);
		assertNotNull(leafCallers);
		assertEquals(0, leafCallers.size());
		
		leafCallers = callGraph.getLeafCallers(B_METHODD);
		assertNotNull(leafCallers);
		assertEquals(1, leafCallers.size());
		assertTrue(leafCallers.contains(A_METHODA));
	}
	
	@Test
	public void testGetLeafCallees() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		
		Set<Method> leafCallees = callGraph.getLeafCallees(A_METHODA);
		assertNotNull(leafCallees);
		assertEquals(2, leafCallees.size());
		assertTrue(leafCallees.contains(A_METHODB));
		assertTrue(leafCallees.contains(B_METHODD));
		
		leafCallees = callGraph.getLeafCallees(B_METHODD);
		assertNotNull(leafCallees);
		assertEquals(0, leafCallees.size());
	}
	
	@Test
	public void testTraverseUsedTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD); 
		callGraph.addCall(B_METHODD, C_METHODE);
		GraphVisitor<Clazz, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsedTypes(A, visitor);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(A, 0);
		verify(visitor).visitEdge(B_METHODD, 1);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitEdge(C_METHODE, 2);
		verify(visitor).visitVertex(C, 2);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testTraverseUsingTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		callGraph.addCall(B_METHODD, C_METHODE);
		GraphVisitor<Clazz, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsingTypes(C, visitor);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(C, 0);
		verify(visitor).visitEdge(C_METHODE, 1);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitEdge(B_METHODD, 2);
		verify(visitor).visitVertex(A, 2);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testGetLeafUsedTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		callGraph.addCall(B_METHODD, C_METHODE);
		
		Set<Clazz> leaves = callGraph.getLeafUsedTypes(A);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(C));
	}
	
	@Test
	public void testGetLeafUsingTypes() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB); // A --> A
		callGraph.addCall(A_METHODA, A_METHODC); // A --> A
		callGraph.addCall(A_METHODC, B_METHODD); // A --> B
		callGraph.addCall(B_METHODD, C_METHODE); // B --> C
		
		Set<Clazz> leaves = callGraph.getLeafUsingTypes(C);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(A));
	}
	
	@Test
	public void testGetDependencyCycles() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD);
		callGraph.addCall(B_METHODD, C_METHODE);
		callGraph.addCall(C_METHODE, D_METHODF);
		callGraph.addCall(D_METHODF, C_METHODE);// first cycle C --> D --> C: This must be ignored, because D inherits C!
		callGraph.addCall(B_METHODD, A_METHODA);// second cylce A --> B --> A

		List<List<Clazz>> cycles = callGraph.getDependencyCycles();
		assertNotNull(cycles);
		assertEquals(1, cycles.size());
		List<Clazz> setABA = cycles.get(0);

		assertEquals(2, setABA.size());
		assertTrue(setABA.contains(A));
		assertTrue(setABA.contains(B));
	}
	
	@Test
	public void testTraverseUsedPackages() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD); 
		callGraph.addCall(B_METHODD, C_METHODE);
		GraphVisitor<Package, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsedPackages(ABC, visitor);
		verify(visitor, times(2)).finished();
		verify(visitor).visitVertex(ABC, 0);
		verify(visitor).visitEdge(B_METHODD, 1);
		verify(visitor).visitVertex(DEF, 1);
		verify(visitor).visitEdge(C_METHODE, 2);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraverseUsingPackages() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, A_METHODB);
		callGraph.addCall(A_METHODA, A_METHODC);
		callGraph.addCall(A_METHODC, B_METHODD); 
		callGraph.addCall(B_METHODD, C_METHODE);
		GraphVisitor<Package, Method> visitor = mock(GraphVisitor.class);

		callGraph.traverseUsingPackages(DEF, visitor);
		verify(visitor, times(2)).finished();
		verify(visitor).visitVertex(DEF, 0);
		verify(visitor).visitEdge(B_METHODD, 1);
		verify(visitor).visitVertex(ABC, 1);
		verify(visitor).visitEdge(C_METHODE, 2);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testGetLeafUsedPackages() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, B_METHODD);
		callGraph.addCall(A_METHODA, C_METHODE);
		callGraph.addCall(A_METHODC, D_METHODF);
		
		Set<Package> leaves = callGraph.getLeafUsedPackages(ABC);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(DEF));
	}
	
	@Test
	public void testGetLeafUsingPackages() {
		CallGraph callGraph = new CallGraph(createClassContext());
		callGraph.addCall(A_METHODA, B_METHODD);
		callGraph.addCall(A_METHODA, C_METHODE);
		callGraph.addCall(A_METHODC, D_METHODF);
		
		Set<Package> leaves = callGraph.getLeafUsingPackages(DEF);
		assertNotNull(leaves);
		assertEquals(1, leaves.size());
		assertTrue(leaves.contains(ABC));
	}
}