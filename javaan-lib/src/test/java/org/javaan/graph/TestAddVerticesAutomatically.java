package org.javaan.graph;

import static org.junit.Assert.*;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

public class TestAddVerticesAutomatically {
	
	@Test
	public void testAddEdgeWithMissingVertices() {
		Graph<String, String> graph = new DefaultDirectedGraph<>(new EdgeFactory<String, String>() {
			public String createEdge(String sourceVertex, String targetVertex) {
				return sourceVertex + targetVertex;
			};
		});
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
