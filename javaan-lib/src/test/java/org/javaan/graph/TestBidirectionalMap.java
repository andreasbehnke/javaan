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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class TestBidirectionalMap {

	@Test
	public void testAddParent() {
		BidirectionalMap<String, String> graph = new BidirectionalMap<String, String>();
		graph.addParent("parent");
		assertTrue(graph.containsParent("parent"));
	}
	
	@Test
	public void testContainsParent() {
		assertFalse(new BidirectionalMap<String, String>().containsParent("unknown"));
	}
	
	@Test
	public void testAddEdge() {
		BidirectionalMap<String, String> graph = new BidirectionalMap<String, String>();
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
		assertFalse(new BidirectionalMap<String, String>().containsChild("unknown"));
	}
	
	@Test
	public void testGetChilds() {
		BidirectionalMap<String, String> graph = new BidirectionalMap<String, String>();
		graph.addParent("parent");
		
		Set<String> childs = graph.getChilds("parent");
		assertNotNull(childs);
		assertEquals(0, childs.size());
		
		graph.addEdge("parent", "child1");
		graph.addEdge("parent", "child2");
		graph.addEdge("parent2", "child3");
		
		childs = graph.getChilds();
		assertNotNull(childs);
		assertEquals(3, childs.size());
		assertTrue(childs.contains("child1"));
		assertTrue(childs.contains("child2"));
		assertTrue(childs.contains("child3"));
	}
	
	@Test
	public void testGetParents() {
		BidirectionalMap<String, String> graph = new BidirectionalMap<String, String>();
		graph.addEdge("parent", "first");
		graph.addEdge("parent2", "first");
		
		Set<String> parents = graph.getParents("first");
		assertNotNull(parents);
		assertEquals(2, parents.size());
		assertTrue(parents.contains("parent"));
		assertTrue(parents.contains("parent2"));
	}
}
