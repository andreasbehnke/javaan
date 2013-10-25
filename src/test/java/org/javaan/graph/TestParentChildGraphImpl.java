package org.javaan.graph;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TestParentChildGraphImpl {

	@Test
	public void testAddParent() {
		ParentChildGraph<String, String> graph = new ParentChildGraphImpl<String, String>();
		graph.addParent("parent");
		assertTrue(graph.containsParent("parent"));
	}
	
	@Test
	public void testContainsParent() {
		assertFalse(new ParentChildGraphImpl<String, String>().containsParent("unknown"));
	}
	
	@Test
	public void testAddEdge() {
		ParentChildGraph<String, String> graph = new ParentChildGraphImpl<String, String>();
		graph.addEdge("parent", "first");
		graph.addEdge("parent", "second");
		
		assertTrue(graph.containsParent("parent"));
		assertTrue(graph.containsChild("first"));
		assertTrue(graph.containsChild("second"));
		Set<String> childs = graph.getChilds("parent");
		assertNotNull(childs);
		assertEquals(2, childs.size());
		assertTrue(childs.contains("first"));
		assertTrue(childs.contains("second"));
	}
	
	@Test
	public void testContainsCild() {
		assertFalse(new ParentChildGraphImpl<String, String>().containsChild("unknown"));
	}
	
	@Test
	public void testGetChilds() {
		ParentChildGraph<String, String> graph = new ParentChildGraphImpl<String, String>();
		graph.addParent("parent");
		
		Set<String> childs = graph.getChilds("parent");
		assertNotNull(childs);
		assertEquals(0, childs.size());
	}
	
	@Test
	public void testGetParents() {
		ParentChildGraph<String, String> graph = new ParentChildGraphImpl<String, String>();
		graph.addEdge("parent", "first");
		graph.addEdge("parent2", "first");
		
		Set<String> parents = graph.getParents("first");
		assertNotNull(parents);
		assertEquals(2, parents.size());
		assertTrue(parents.contains("parent"));
		assertTrue(parents.contains("parent2"));
	}
}
