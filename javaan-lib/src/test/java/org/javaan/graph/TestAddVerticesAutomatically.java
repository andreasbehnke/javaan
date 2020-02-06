package org.javaan.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestAddVerticesAutomatically {
	
	@Test
	public void testAddEdgeWithMissingVertices() {
		Graph<String, String> graph = new DefaultDirectedGraph<String, String>(null, null, false) {
			@Override
			public String addEdge(String sourceVertex, String targetVertex) {
				String edge = sourceVertex + targetVertex;
				super.addEdge(sourceVertex, targetVertex, edge);
				return edge;
			}
		};
		graph = new AddVerticesAutomatically<>(graph);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C", "DEF");
		assertTrue(graph.containsVertex("A"));
		assertTrue(graph.containsVertex("B"));
		assertTrue(graph.containsVertex("C"));
		assertTrue(graph.containsEdge("AB"));
		assertTrue(graph.containsEdge("DEF"));
	}

}
