package org.javaan.graph;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestSingleChildGraphImpl {
	
	@Test
	public void testAddNode() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addNode("a");
		
		assertTrue(graph.containsNode("a"));
		
		graph.addEdge("b", "c");
		// relation should not be overwritten
		graph.addNode("b");
		assertEquals("c", graph.getChild("b"));
	}
	
	@Test
	public void testAddEdge() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		
		assertTrue(graph.containsNode("a"));
		assertTrue(graph.containsNode("x"));
		assertNull(graph.getChild("a"));
		assertEquals("a", graph.getChild("x"));
	}
	
	@Test
	public void testGetNodes() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addNode("a");
		graph.addEdge("x", "b");
		
		Set<String> nodes = graph.getNodes();
		assertNotNull(nodes);
		assertEquals(3, nodes.size());
		assertTrue(nodes.contains("a"));
		assertTrue(nodes.contains("b"));
		assertTrue(nodes.contains("x"));
	}

	@Test
	public void testGetChild() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		graph.addEdge("x", "c");
		
		assertEquals("c", graph.getChild("x"));
	}
	
	@Test
	public void testGetParents() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("a", "x");
		graph.addEdge("b", "x");
		graph.addEdge("c", "x");
		
		Set<String> parents = graph.getParents("x");
		assertNotNull(parents);
		assertEquals(3, parents.size());
		assertTrue(parents.contains("a"));
		assertTrue(parents.contains("b"));
		assertTrue(parents.contains("c"));
	}
	
	@Test
	public void testHasChild() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addEdge("x", "a");
		
		assertTrue(graph.hasChild("x"));
		assertFalse(graph.hasChild("a"));
	}
	
	@Test
	public void testContainsNode() {
		SingleChildGraph<String> graph = new SingleChildGraphImpl<String>();
		graph.addNode("x");
		assertTrue(graph.containsNode("x"));
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
