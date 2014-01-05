package org.codeforest.graph;

import static org.junit.Assert.assertEquals;

import org.codeforest.graph.TreeWidthCalculator;
import org.codeforest.model.VertexSceneContext;
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
		
		VertexSceneContext<String> context = new VertexSceneContext<String>();
		new TreeWidthCalculator<String, String>(context, graph).calculateVertexWidth(A);
		assertEquals(1, context.get(H).getSubTreeWidth());
		assertEquals(1, context.get(G).getSubTreeWidth());
		assertEquals(1, context.get(E).getSubTreeWidth());
		assertEquals(1, context.get(D).getSubTreeWidth());
		assertEquals(1, context.get(F).getSubTreeWidth());
		assertEquals(1, context.get(C).getSubTreeWidth());
		assertEquals(3, context.get(B).getSubTreeWidth());
		assertEquals(4, context.get(A).getSubTreeWidth());
	}
	
}
