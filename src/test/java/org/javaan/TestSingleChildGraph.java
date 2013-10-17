package org.javaan;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TestSingleChildGraph {

	@Test
	public void testAddEdge() {
		SingleChildGraph<String> graph = new SingleChildGraph<String>();
		
		graph.addNode("a");
		Set<String> childs = graph.getChilds("a");
		assertNotNull(childs);
		assertEquals(0, childs.size());
		
		graph.addEdge("a", "b");
		assertTrue(graph.containsNode("b"));
		childs = graph.getChilds("a");
		assertNotNull(childs);
		assertEquals(1, childs.size());
		assertTrue(childs.contains("b"));
		
		graph.addEdge("a", "c");
		assertTrue(graph.containsNode("c"));
		childs = graph.getChilds("a");
		assertNotNull(childs);
		assertEquals(1, childs.size());
		assertTrue(childs.contains("c"));
	}
	
}