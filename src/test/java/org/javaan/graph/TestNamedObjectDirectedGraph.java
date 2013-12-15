package org.javaan.graph;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.javaan.model.Clazz;
import org.junit.Test;

public class TestNamedObjectDirectedGraph {

	private static final Clazz A = new Clazz("a");
	private static final Clazz B = new Clazz("b");
	private static final Clazz C = new Clazz("c");
	private static final Clazz D = new Clazz("d");
	private static final Clazz E = new Clazz("e");
	private static final Clazz F = new Clazz("f");
	private static final Clazz G = new Clazz("g");
	private static final Clazz X = new Clazz("x");
	private static final Clazz Y = new Clazz("y");

	@Test
	public void testTargetVerticesOf() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		
		Set<Clazz> childs = graph.targetVerticesOf(B);
		assertNotNull(childs);
		assertEquals(0, childs.size());
		
		childs = graph.targetVerticesOf(A);
		assertNotNull(childs);
		assertEquals(0, childs.size());
		
		childs = graph.targetVerticesOf(X);
		assertNotNull(childs);
		assertEquals(3, childs.size());
		assertTrue(childs.contains(A));
		assertTrue(childs.contains(B));
		assertTrue(childs.contains(C));
	}

	@Test
	public void testSourceVerticesOf() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(Y, A);
		
		Set<Clazz> childs = graph.sourceVerticesOf(B);
		assertNotNull(childs);
		assertEquals(1, childs.size());
		assertTrue(childs.contains(X));
		
		childs = graph.sourceVerticesOf(A);
		assertNotNull(childs);
		assertEquals(2, childs.size());
		assertTrue(childs.contains(X));
		assertTrue(childs.contains(Y));
		
		childs = graph.sourceVerticesOf(X);
		assertNotNull(childs);
		assertEquals(0, childs.size());
	}
	
	@Test
	public void testSuccessorsOf() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(B, C);
		
		Set<Clazz> successors = graph.successorsOf(X);
		assertNotNull(successors);
		assertEquals(3, successors.size());
		assertTrue(successors.contains(A));
		assertTrue(successors.contains(B));
		assertTrue(successors.contains(C));
	}

	@Test
	public void testPredecessorsOf() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(B, C);
		graph.addEdge(D, C);
		
		Set<Clazz> predecessors = graph.predecessorsOf(X);
		assertNotNull(predecessors);
		assertEquals(0, predecessors.size());
		
		predecessors = graph.predecessorsOf(A);
		assertNotNull(predecessors);
		assertEquals(1, predecessors.size());
		assertTrue(predecessors.contains(X));
		
		predecessors = graph.predecessorsOf(B);
		assertNotNull(predecessors);
		assertEquals(1, predecessors.size());
		assertTrue(predecessors.contains(X));
		
		predecessors = graph.predecessorsOf(C);
		assertNotNull(predecessors);
		assertEquals(3, predecessors.size());
		assertTrue(predecessors.contains(B));
		assertTrue(predecessors.contains(D));
		assertTrue(predecessors.contains(X));
	}

	@Test
	public void testTraverseSuccessorsDepthFirst() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		VertexEdge<Clazz> X_A = graph.addEdge(X, A);
		VertexEdge<Clazz> X_B = graph.addEdge(X, B);
		VertexEdge<Clazz> X_C = graph.addEdge(X, C);
		VertexEdge<Clazz> C_D = graph.addEdge(C, D);
		VertexEdge<Clazz> C_E = graph.addEdge(C, E);
		VertexEdge<Clazz> E_F = graph.addEdge(E, F);
		VertexEdge<Clazz> X_G = graph.addEdge(X, G);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);
		
		graph.traverseSuccessorsDepthFirst(X, visitor);
		verify(visitor, times(8)).finished();
		verify(visitor).visitVertex(X, 0);
		verify(visitor).visitEdge(X_A, 0);
		verify(visitor).visitVertex(A, 1);
		verify(visitor).visitEdge(X_B, 0);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitEdge(X_C, 0);
		verify(visitor).visitVertex(C, 1);
		verify(visitor).visitEdge(C_D, 1);
		verify(visitor).visitVertex(D, 2);
		verify(visitor).visitEdge(C_E, 1);
		verify(visitor).visitVertex(E, 2);
		verify(visitor).visitEdge(E_F, 2);
		verify(visitor).visitVertex(F, 3);
		verify(visitor).visitEdge(X_G, 0);
		verify(visitor).visitVertex(G, 1);
		verifyNoMoreInteractions(visitor);
	}
	
	@Test
	public void testTraverseSuccessorsBreadthFirst() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		VertexEdge<Clazz> X_A = graph.addEdge(X, A);
		VertexEdge<Clazz> X_B = graph.addEdge(X, B);
		VertexEdge<Clazz> X_C = graph.addEdge(X, C);
		VertexEdge<Clazz> C_D = graph.addEdge(C, D);
		VertexEdge<Clazz> C_E = graph.addEdge(C, E);
		VertexEdge<Clazz> E_F = graph.addEdge(E, F);
		VertexEdge<Clazz> X_G = graph.addEdge(X, G);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);
		
		graph.traverseSuccessorsBreadthFirst(X, visitor);
		verify(visitor, times(8)).finished();
		verify(visitor).visitVertex(X, -1);
		verify(visitor).visitEdge(X_A, -1);
		verify(visitor).visitVertex(A, -1);
		verify(visitor).visitEdge(X_B, -1);
		verify(visitor).visitVertex(B, -1);
		verify(visitor).visitEdge(X_C, -1);
		verify(visitor).visitVertex(C, -1);
		verify(visitor).visitEdge(X_G, -1);
		verify(visitor).visitVertex(G, -1);
		verify(visitor).visitEdge(C_D, -1);
		verify(visitor).visitVertex(D, -1);
		verify(visitor).visitEdge(C_E, -1);
		verify(visitor).visitVertex(E, -1);
		verify(visitor).visitEdge(E_F, -1);
		verify(visitor).visitVertex(F, -1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraverseSuccessorsDepthFirstCycle() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		VertexEdge<Clazz> X_A = graph.addEdge(X, A);
		VertexEdge<Clazz> A_B = graph.addEdge(A, B);
		VertexEdge<Clazz> B_X = graph.addEdge(B, X);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);

		graph.traverseSuccessorsDepthFirst(X, visitor);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(X, 0);
		verify(visitor).visitEdge(X_A, 0);
		verify(visitor).visitVertex(A, 1);
		verify(visitor).visitEdge(A_B, 1);
		verify(visitor).visitVertex(B, 2);
		verify(visitor).visitEdge(B_X, 2);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraverseSuccessorsDepthFirstDisruption() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		graph.addEdge(X, G);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);
		when(visitor.finished()).thenReturn(true);
		
		graph.traverseSuccessorsDepthFirst(X, visitor);
		verify(visitor).finished();
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraverseSuccessorsDepthFirstUnknownStartVertex() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);

		graph.traverseSuccessorsDepthFirst(X, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraversePredecessorsDepthFirst() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		VertexEdge<Clazz> X_C = graph.addEdge(X, C);
		graph.addEdge(C, D);
		VertexEdge<Clazz> C_E = graph.addEdge(C, E);
		VertexEdge<Clazz> E_F = graph.addEdge(E, F);
		VertexEdge<Clazz> Y_F = graph.addEdge(Y, F);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);
		
		graph.traversePredecessorsDepthFirst(F, visitor);
		verify(visitor, times(5)).finished();
		verify(visitor).visitVertex(F, 0);
		verify(visitor).visitEdge(E_F, 0);
		verify(visitor).visitVertex(E, 1);
		verify(visitor).visitEdge(C_E, 1);
		verify(visitor).visitVertex(C, 2);
		verify(visitor).visitEdge(X_C, 2);
		verify(visitor).visitVertex(X, 3);
		verify(visitor).visitEdge(Y_F, 0);
		verify(visitor).visitVertex(Y, 1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraversePredecessorsBreadthFirst() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		VertexEdge<Clazz> X_C = graph.addEdge(X, C);
		graph.addEdge(C, D);
		VertexEdge<Clazz> C_E = graph.addEdge(C, E);
		VertexEdge<Clazz> E_F = graph.addEdge(E, F);
		VertexEdge<Clazz> Y_F = graph.addEdge(Y, F);
		NamedObjectVisitor<Clazz> visitor = mock(NamedObjectVisitor.class);
		
		graph.traversePredecessorsBreadthFirst(F, visitor);
		verify(visitor, times(5)).finished();
		verify(visitor).visitVertex(F, -1);
		verify(visitor).visitEdge(E_F, -1);
		verify(visitor).visitVertex(E, -1);
		verify(visitor).visitEdge(Y_F, -1);
		verify(visitor).visitVertex(Y, -1);
		verify(visitor).visitEdge(C_E, -1);
		verify(visitor).visitVertex(C, -1);
		verify(visitor).visitEdge(X_C, -1);
		verify(visitor).visitVertex(X, -1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testGetLeafSuccessors() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		
		Set<Clazz> leafNodes = graph.getLeafSuccessors(X);
		assertNotNull(leafNodes);
		assertEquals(4, leafNodes.size());
		assertTrue(leafNodes.contains(A));
		assertTrue(leafNodes.contains(B));
		assertTrue(leafNodes.contains(D));
		assertTrue(leafNodes.contains(F));
		leafNodes = graph.getLeafSuccessors(C);
		assertNotNull(leafNodes);
		assertEquals(2, leafNodes.size());
		assertTrue(leafNodes.contains(D));
		assertTrue(leafNodes.contains(F));
		leafNodes = graph.getLeafSuccessors(A);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.getLeafSuccessors(B);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.getLeafSuccessors(E);
		assertNotNull(leafNodes);
		assertEquals(1, leafNodes.size());
		assertTrue(leafNodes.contains(F));
	}

	@Test
	public void testGetLeafSuccessorsUnknownStartVertex() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		Set<Clazz> leafNodes = graph.getLeafSuccessors(X);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
	}

	@Test
	public void testGetLeafPredecessors() {
		NamedObjectDirectedGraph<Clazz> graph = new NamedObjectDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		
		Set<Clazz> leafNodes = graph.getLeafPredecessors(X);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.getLeafPredecessors(C);
		assertNotNull(leafNodes);
		assertEquals(1, leafNodes.size());
		assertTrue(leafNodes.contains(X));
	}
}