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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class TestExtendedDirectedGraph {

	private static final String A = "a";
	private static final String B = "b";
	private static final String C = "c";
	private static final String D = "d";
	private static final String E = "e";
	private static final String F = "f";
	private static final String G = "g";
	private static final String X = "x";
	private static final String Y = "y";
	
	private static <V> ExtendedDirectedGraph<V, VertexEdge<V>> createVertexEdgeDirectedGraph() {
		return new ExtendedDirectedGraph<>(
				new DefaultDirectedGraph<V, VertexEdge<V>>(
						new VertexEdgeFactory<V>())
		);
	}

	@Test
	public void testTargetVerticesOf() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		
		Set<String> childs = graph.targetVerticesOf(B);
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
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(Y, A);
		
		Set<String> childs = graph.sourceVerticesOf(B);
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
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(B, C);
		
		Set<String> successors = graph.successorsOf(X);
		assertNotNull(successors);
		assertEquals(3, successors.size());
		assertTrue(successors.contains(A));
		assertTrue(successors.contains(B));
		assertTrue(successors.contains(C));
	}

	@Test
	public void testPredecessorsOf() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(B, C);
		graph.addEdge(D, C);
		
		Set<String> predecessors = graph.predecessorsOf(X);
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
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		VertexEdge<String> X_A = graph.addEdge(X, A);
		VertexEdge<String> X_B = graph.addEdge(X, B);
		VertexEdge<String> X_C = graph.addEdge(X, C);
		VertexEdge<String> C_D = graph.addEdge(C, D);
		VertexEdge<String> C_E = graph.addEdge(C, E);
		VertexEdge<String> E_F = graph.addEdge(E, F);
		VertexEdge<String> X_G = graph.addEdge(X, G);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);
		
		graph.traverseDepthFirst(X, visitor, false);
		verify(visitor, times(8)).finished();
		verify(visitor).visitVertex(X, 0);
		verify(visitor).visitEdge(X_A, 1);
		verify(visitor).visitVertex(A, 1);
		verify(visitor).visitEdge(X_B, 1);
		verify(visitor).visitVertex(B, 1);
		verify(visitor).visitEdge(X_C, 1);
		verify(visitor).visitVertex(C, 1);
		verify(visitor).visitEdge(C_D, 2);
		verify(visitor).visitVertex(D, 2);
		verify(visitor).visitEdge(C_E, 2);
		verify(visitor).visitVertex(E, 2);
		verify(visitor).visitEdge(E_F, 3);
		verify(visitor).visitVertex(F, 3);
		verify(visitor).visitEdge(X_G, 1);
		verify(visitor).visitVertex(G, 1);
	}
	
	@Test
	public void testTraverseSuccessorsBreadthFirst() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		VertexEdge<String> X_A = graph.addEdge(X, A);
		VertexEdge<String> X_B = graph.addEdge(X, B);
		VertexEdge<String> X_C = graph.addEdge(X, C);
		VertexEdge<String> C_D = graph.addEdge(C, D);
		VertexEdge<String> C_E = graph.addEdge(C, E);
		VertexEdge<String> E_F = graph.addEdge(E, F);
		VertexEdge<String> X_G = graph.addEdge(X, G);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);
		
		graph.traverseBreadthFirst(X, visitor, false);
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
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		VertexEdge<String> X_A = graph.addEdge(X, A);
		VertexEdge<String> A_B = graph.addEdge(A, B);
		VertexEdge<String> B_X = graph.addEdge(B, X);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);

		graph.traverseDepthFirst(X, visitor, false);
		verify(visitor, times(3)).finished();
		verify(visitor).visitVertex(X, 0);
		verify(visitor).visitEdge(X_A, 1);
		verify(visitor).visitVertex(A, 1);
		verify(visitor).visitEdge(A_B, 2);
		verify(visitor).visitVertex(B, 2);
		verify(visitor).visitEdge(B_X, 3);
	}

	@Test
	public void testTraverseSuccessorsDepthFirstDisruption() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		graph.addEdge(X, G);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);
		when(visitor.finished()).thenReturn(true);
		
		graph.traverseDepthFirst(X, visitor, false);
		verify(visitor).finished();
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraverseSuccessorsDepthFirstUnknownStartVertex() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);

		graph.traverseDepthFirst(X, visitor, false);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void testTraversePredecessorsDepthFirst() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		VertexEdge<String> X_C = graph.addEdge(X, C);
		graph.addEdge(C, D);
		VertexEdge<String> C_E = graph.addEdge(C, E);
		VertexEdge<String> E_F = graph.addEdge(E, F);
		VertexEdge<String> Y_F = graph.addEdge(Y, F);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);
		
		graph.traverseDepthFirst(F, visitor, true);
		verify(visitor, times(5)).finished();
		verify(visitor).visitVertex(F, 0);
		verify(visitor).visitEdge(E_F, 1);
		verify(visitor).visitVertex(E, 1);
		verify(visitor).visitEdge(C_E, 2);
		verify(visitor).visitVertex(C, 2);
		verify(visitor).visitEdge(X_C, 3);
		verify(visitor).visitVertex(X, 3);
		verify(visitor).visitEdge(Y_F, 1);
		verify(visitor).visitVertex(Y, 1);
	}

	@Test
	public void testTraversePredecessorsBreadthFirst() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		VertexEdge<String> X_C = graph.addEdge(X, C);
		graph.addEdge(C, D);
		VertexEdge<String> C_E = graph.addEdge(C, E);
		VertexEdge<String> E_F = graph.addEdge(E, F);
		VertexEdge<String> Y_F = graph.addEdge(Y, F);
		VertexEdgeGraphVisitor<String> visitor = mock(VertexEdgeGraphVisitor.class);
		
		graph.traverseBreadthFirst(F, visitor, true);
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
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		
		Set<String> leafNodes = graph.collectLeaves(X, false);
		assertNotNull(leafNodes);
		assertEquals(4, leafNodes.size());
		assertTrue(leafNodes.contains(A));
		assertTrue(leafNodes.contains(B));
		assertTrue(leafNodes.contains(D));
		assertTrue(leafNodes.contains(F));
		leafNodes = graph.collectLeaves(C, false);
		assertNotNull(leafNodes);
		assertEquals(2, leafNodes.size());
		assertTrue(leafNodes.contains(D));
		assertTrue(leafNodes.contains(F));
		leafNodes = graph.collectLeaves(A, false);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.collectLeaves(B, false);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.collectLeaves(E, false);
		assertNotNull(leafNodes);
		assertEquals(1, leafNodes.size());
		assertTrue(leafNodes.contains(F));
	}

	@Test
	public void testGetLeafSuccessorsUnknownStartVertex() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		Set<String> leafNodes = graph.collectLeaves(A, false);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
	}

	@Test
	public void testGetLeafPredecessors() {
		ExtendedDirectedGraph<String, VertexEdge<String>> graph = createVertexEdgeDirectedGraph();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		graph.addEdge(X, C);
		graph.addEdge(C, D);
		graph.addEdge(C, E);
		graph.addEdge(E, F);
		
		Set<String> leafNodes = graph.collectLeaves(X, true);
		assertNotNull(leafNodes);
		assertEquals(0, leafNodes.size());
		leafNodes = graph.collectLeaves(C, true);
		assertNotNull(leafNodes);
		assertEquals(1, leafNodes.size());
		assertTrue(leafNodes.contains(X));
	}
}