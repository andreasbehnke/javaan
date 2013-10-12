package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class TestGraph {
	
	@Test
	public void testAddNode() {
		Graph graph = new Graph();
		graph.addNode("a");
		
		assertTrue(graph.containsNode("a"));
	}
	
	@Test
	public void testAddEdge() {
		Graph graph = new Graph();
		graph.addEdge("x", "a");
		
		assertTrue(graph.containsNode("a"));
		assertTrue(graph.containsNode("x"));
		Set<String> childs = graph.getChilds("a");
		assertNotNull(childs);
		assertEquals(0, childs.size());
		childs = graph.getChilds("x");
		assertNotNull(childs);
		assertEquals(1, childs.size());
		assertTrue(childs.contains("a"));
	}

	@Test
	public void testGetChilds() {
		Graph graph = new Graph();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		graph.addEdge("x", "c");
		
		Set<String> childs = graph.getChilds("x");
		assertNotNull(childs);
		assertTrue(childs.contains("a"));
		assertTrue(childs.contains("b"));
		assertTrue(childs.contains("c"));
	}
	
	@Test
	public void testHasChilds() {
		Graph childs = new Graph();
		childs.addEdge("x", "a");
		
		assertTrue(childs.hasChilds("x"));
		assertFalse(childs.hasChilds("a"));
	}
	
	@Test
	public void testContainsNode() {
		Graph childs = new Graph();
		childs.addNode("x");
		
		assertTrue(childs.containsNode("x"));
	}
	
	@Test
	public void testGetLeaveNodes() {
		Graph graph = new Graph();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		graph.addEdge("x", "c");
		graph.addEdge("c", "d");
		graph.addEdge("c", "e");
		graph.addEdge("e", "f");
		
		Set<String> leaveNodes = graph.getLeaveNodes("x");
		assertNotNull(leaveNodes);
		assertEquals(4, leaveNodes.size());
		assertTrue(leaveNodes.contains("a"));
		assertTrue(leaveNodes.contains("b"));
		assertTrue(leaveNodes.contains("d"));
		assertTrue(leaveNodes.contains("f"));
		leaveNodes = graph.getLeaveNodes("c");
		assertNotNull(leaveNodes);
		assertEquals(2, leaveNodes.size());
		assertTrue(leaveNodes.contains("d"));
		assertTrue(leaveNodes.contains("f"));
		leaveNodes = graph.getLeaveNodes("a");
		assertNotNull(leaveNodes);
		assertEquals(0, leaveNodes.size());
		leaveNodes = graph.getLeaveNodes("b");
		assertNotNull(leaveNodes);
		assertEquals(0, leaveNodes.size());
		leaveNodes = graph.getLeaveNodes("e");
		assertNotNull(leaveNodes);
		assertEquals(1, leaveNodes.size());
		assertTrue(leaveNodes.contains("f"));
	}
	
	@Test
	public void testGetCallingEntryMethodsCycle() {
		Graph graph = new Graph();
		graph.addEdge("x", "a");
		graph.addEdge("a", "b");
		graph.addEdge("b", "x");
		graph.addEdge("a", "c");
		
		Set<String> leaveNodes = graph.getLeaveNodes("x");
		assertNotNull(leaveNodes);
		assertEquals(1, leaveNodes.size());
		assertTrue(leaveNodes.contains("c"));
	}
}
