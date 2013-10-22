package org.javaan.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class TestSingleChildGraphImpl {
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		fail("expecting exception");
	}

	@Test
	public void testGetChild() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		assertEquals("a", graph.getChild("x"));
	}
	
	@Test
	public void testGetPath() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("a", "b");
		graph.addEdge("b", "c");
		graph.addEdge("c", "d");
		graph.addEdge("x", "y");
		graph.addNode("z");
		
		List<String> path = graph.getPath("a");
		assertNotNull(path);
		assertEquals(4, path.size());
		assertEquals("a", path.get(0));
		assertEquals("b", path.get(1));
		assertEquals("c", path.get(2));
		assertEquals("d", path.get(3));
		
		path = graph.getPath("x");
		assertNotNull(path);
		assertEquals(2, path.size());
		assertEquals("x", path.get(0));
		assertEquals("y", path.get(1));
		
		path = graph.getPath("z");
		assertNotNull(path);
		assertEquals(1, path.size());
		assertEquals("z", path.get(0));
	}
	
	@Test
	public void testGetPathCycle() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		graph.addEdge("a", "b");
		graph.addEdge("b", "x");

		List<String> path = graph.getPath("x");
		assertNotNull(path);
		assertEquals(4, path.size());
		assertEquals("x", path.get(0));
		assertEquals("a", path.get(1));
		assertEquals("b", path.get(2));
		assertEquals("x", path.get(3));
	}
}
