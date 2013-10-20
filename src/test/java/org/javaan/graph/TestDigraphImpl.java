package org.javaan.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.Graph;
import org.javaan.graph.DigraphImpl;
import org.junit.Test;

public class TestDigraphImpl {
	
	@Test
	public void testAddNode() {
		Digraph<String> graph = new DigraphImpl<String>();
		graph.addNode("a");
		assertTrue(graph.containsNode("a"));
		
		graph.addEdge("b", "c");
		// relation should not be overwritten
		graph.addNode("b");
		Set<String> childs = graph.getChilds("b");
		assertNotNull(childs);
		assertEquals(1, childs.size());
		assertTrue(childs.contains("c"));
	}
	
	@Test
	public void testAddEdge() {
		Digraph<String> graph = new DigraphImpl<String>();
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
	public void testGetNodes() {
		Graph<String> graph = new DigraphImpl<String>();
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
	public void testGetChilds() {
		Digraph<String> graph = new DigraphImpl<String>();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		graph.addEdge("x", "c");
		
		Set<String> childs = graph.getChilds("x");
		assertNotNull(childs);
		assertEquals(3, childs.size());
		assertTrue(childs.contains("a"));
		assertTrue(childs.contains("b"));
		assertTrue(childs.contains("c"));
	}
	
	@Test
	public void testGetParents() {
		Digraph<String> graph = new DigraphImpl<String>();
		graph.addEdge("a", "x");
		graph.addEdge("b", "x");
		graph.addEdge("c", "x");
		
		Set<String> childs = graph.getParents("x");
		assertNotNull(childs);
		assertEquals(3, childs.size());
		assertTrue(childs.contains("a"));
		assertTrue(childs.contains("b"));
		assertTrue(childs.contains("c"));
	}
	
	@Test
	public void testGetSuccessors() {
		Digraph<String> graph = new DigraphImpl<String>();
		graph.addEdge("x", "a");
		graph.addEdge("x", "b");
		graph.addEdge("b", "c");
		
		Set<String> successors = graph.getSuccessors("x");
		assertNotNull(successors);
		assertEquals(3, successors.size());
		assertTrue(successors.contains("a"));
		assertTrue(successors.contains("b"));
		assertTrue(successors.contains("c"));
	}
	
	@Test
	public void testHasChilds() {
		Digraph<String> graph = new DigraphImpl<String>();
		graph.addEdge("x", "a");
		
		assertTrue(graph.hasChilds("x"));
		assertFalse(graph.hasChilds("a"));
	}
	
	@Test
	public void testContainsNode() {
		Graph<String> graph = new DigraphImpl<String>();
		graph.addNode("x");
		
		assertTrue(graph.containsNode("x"));
	}
	
	@Test
	public void testGetLeaveNodes() {
		Digraph<String> graph = new DigraphImpl<String>();
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
	public void testGetLeaveNodesCycle() {
		Digraph<String> graph = new DigraphImpl<String>();
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
