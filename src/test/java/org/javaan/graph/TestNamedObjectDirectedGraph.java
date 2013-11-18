package org.javaan.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.javaan.model.Clazz;
import org.junit.Test;

public class TestNamedObjectDirectedGraph {

	private static final Clazz A = new Clazz("a");
	private static final Clazz B = new Clazz("b");
	private static final Clazz C = new Clazz("c");
	private static final Clazz D = new Clazz("d");
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
}