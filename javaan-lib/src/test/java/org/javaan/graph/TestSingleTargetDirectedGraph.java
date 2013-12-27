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
import static org.junit.Assert.fail;

import java.util.List;

import org.javaan.model.Clazz;
import org.junit.Test;

public class TestSingleTargetDirectedGraph {
	
	private final static Clazz A = new Clazz("a");
	private final static Clazz B = new Clazz("b");
	private final static Clazz C = new Clazz("c");
	private final static Clazz D = new Clazz("d");
	private final static Clazz X = new Clazz("x");
	private final static Clazz Y = new Clazz("y");
	private final static Clazz Z = new Clazz("z");
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge() {
		SingleTargetDirectedGraph<Clazz> graph = new SingleTargetDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(X, B);
		fail("expecting exception");
	}

	@Test
	public void testTargetVertexOf() {
		SingleTargetDirectedGraph<Clazz> graph = new SingleTargetDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		assertEquals(A, graph.targetVertexOf(X));
	}
	
	@Test
	public void testSuccessorPathOf() {
		SingleTargetDirectedGraph<Clazz> graph = new SingleTargetDirectedGraph<Clazz>();
		graph.addEdge(A, B);
		graph.addEdge(B, C);
		graph.addEdge(C, D);
		graph.addEdge(X, Y);
		graph.addVertex(Z);
		
		List<Clazz> path = graph.successorPathOf(A);
		assertNotNull(path);
		assertEquals(4, path.size());
		assertEquals(A, path.get(0));
		assertEquals(B, path.get(1));
		assertEquals(C, path.get(2));
		assertEquals(D, path.get(3));
		
		path = graph.successorPathOf(X);
		assertNotNull(path);
		assertEquals(2, path.size());
		assertEquals(X, path.get(0));
		assertEquals(Y, path.get(1));
		
		path = graph.successorPathOf(Z);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertEquals(Z, path.get(0));
	}
	
	@Test
	public void testSuccessorPathOfCycle() {
		SingleTargetDirectedGraph<Clazz> graph = new SingleTargetDirectedGraph<Clazz>();
		graph.addEdge(X, A);
		graph.addEdge(A, B);
		graph.addEdge(B, X);

		List<Clazz> path = graph.successorPathOf(X);
		assertNotNull(path);
		assertEquals(3, path.size());
		assertEquals(X, path.get(0));
		assertEquals(A, path.get(1));
		assertEquals(B, path.get(2));
	}
}
