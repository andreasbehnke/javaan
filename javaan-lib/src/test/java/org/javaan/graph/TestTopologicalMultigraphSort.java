package org.javaan.graph;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;
import org.junit.Test;

public class TestTopologicalMultigraphSort {

	/*
		Example Graph (numbers represent number of edges):
		
		           -----------
		          /           \
        A --2--> B --3--> C   /
                         /   /
        D --3------------   /
       /|\ \               /
        |   -4--> E --1---/   // Circle
        1
        |
        F --3--> G
		
		Subgraphs:
		
		1.)
        A --2--> B --3--> C

        2.)
                   3--> C
                  /
		F --1--> D --4--> E --1--> B
		 \	
		  --3--> G
		  
		Expected Result:
		
		A,B,C,F,G,D,E
	 */
	
	private static final String A = "A";

	private static final String B = "B";

	private static final String C = "C";

	private static final String D = "D";

	private static final String E = "E";

	private static final String F = "F";
	
	private static final String G = "G";
	
	private static void addEdges(Graph<String, String> graph, String source, String target, int count) {
		for(int i=0; i<count; i++) {
			graph.addEdge(source, target);
		}
	}
	
	@Test
	public void testGetVerticesSortedByOutDegree() {
		DirectedGraph<String, String> graph = new DirectedMultigraph<>(new EdgeFactory<String, String>() {
			
			private int count = 0;
			
			@Override
			public String createEdge(String sourceVertex, String targetVertex) {
				count++;
				return sourceVertex + targetVertex + count;
			}
		});
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		graph.addVertex(D);
		graph.addVertex(E);
		graph.addVertex(F);
		graph.addVertex(G);
		addEdges(graph, A, B, 2);
		addEdges(graph, B, C, 3);
		addEdges(graph, D, C, 3);
		addEdges(graph, D, E, 4);
		addEdges(graph, E, B, 1); // cycle connector
		addEdges(graph, F, D, 1);
		addEdges(graph, F, G, 3);
		
		List<String> result = new TopologicalMultigraphSort<String, String>(graph).sort();
		// Expected Result: A,B,C,F,G,D,E
		
		assertEquals(7, result.size());
		assertEquals(A, result.get(0));
		assertEquals(B, result.get(1));
		assertEquals(C, result.get(2));
		assertEquals(F, result.get(3));
		assertEquals(G, result.get(4));
		assertEquals(D, result.get(5));
		assertEquals(E, result.get(6));
	}
}
