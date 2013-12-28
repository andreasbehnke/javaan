package org.javaan.graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

public class TestTreeWidthCalculator {

	private static final String H = "H";
	private static final String G = "G";
	private static final String F = "F";
	private static final String E = "E";
	private static final String D = "D";
	private static final String C = "C";
	private static final String B = "B";
	private static final String A = "A";

	@Test
	public void testCalculateVertexWidth() {
		DirectedGraph<String, String> graph = new DefaultDirectedGraph<String, String>(new EdgeFactory<String, String>() {
			public String createEdge(String sourceVertex, String targetVertex) {
				return sourceVertex + targetVertex;
			};
		});
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		graph.addVertex(D);
		graph.addVertex(E);
		graph.addVertex(F);
		graph.addVertex(G);
		graph.addVertex(H);
		graph.addEdge(A, B);
		graph.addEdge(A, C);
		graph.addEdge(C, D);
		graph.addEdge(B, E);
		graph.addEdge(B, F);
		graph.addEdge(B, G);
		graph.addEdge(F, H);
		
		Map<String, Integer> widths = TreeWidthCalculator.calculateVertexWidth(graph, A);
		assertEquals(1, widths.get(H).intValue());
		assertEquals(1, widths.get(G).intValue());
		assertEquals(1, widths.get(E).intValue());
		assertEquals(1, widths.get(D).intValue());
		assertEquals(1, widths.get(F).intValue());
		assertEquals(1, widths.get(C).intValue());
		assertEquals(3, widths.get(B).intValue());
		assertEquals(4, widths.get(A).intValue());
	}
	
}
