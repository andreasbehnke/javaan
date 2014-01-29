package org.javaan.graph;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.javaan.graph.SimpleGraphReader.ObjectProducer;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

public class TestMinimumEdgesCycleCut {
	
	private static final String GRAPH_CONTENT = 
			"E a b ab1 \n" // first cycle a -> b -> c -> d -> a
			+ "E b c bc1 \n"
			+ "E b c bc2 \n"
			+ "E c d cd1 \n"
			+ "E c d cd2 \n"
			+ "E d a da1 \n"
			+ "E d a da2 \n"
			+ "E d a da3 \n"
			+ "E c e ce1 \n" // second cycle c -> e -> b -> c
			+ "E c e ce2 \n"
			+ "E c e ce3 \n"
			+ "E e b eb1";

	private DirectedPseudograph<String, String> createGraph() throws IOException {
		DirectedPseudograph<String, String> graph = new DirectedPseudograph<String, String>(new UnsupportedEdgeFactory<String, String>());
		ObjectProducer<String, String> objectProducer = new ObjectProducer<String, String>() {
			@Override
			public String createEdge(String source, String target, String edgeLabel) {
				return source + "-->" + target + ":" + edgeLabel;
			}
			
			@Override
			public String createVertex(String vertexLabel) {
				return vertexLabel;
			}
		};
		new SimpleGraphReader<String, String>(graph, objectProducer).readGraph(new StringReader(GRAPH_CONTENT));
		return graph;
	}

	@Test
	public void testFindCutPoints() throws IOException {
		DirectedMultigraph<String, String> target = new DirectedMultigraph<>(new UnsupportedEdgeFactory<String, String>());
		List<CutPoint<String, String>> cutPoints = new MinimumEdgesCycleCut<>(createGraph(), target).findCutPoints();
		assertNotNull(cutPoints);
		assertEquals(2, cutPoints.size());
		assertTrue(cutPoints.contains(new CutPoint<String, String>("a", "b")));
		assertTrue(cutPoints.contains(new CutPoint<String, String>("e", "b")));
	}
	
	@Test
	public void testCutCycles() throws IOException {
		DirectedPseudograph<String, String> source = createGraph();
		DirectedGraph<String, String> target = new DirectedMultigraph<>(new UnsupportedEdgeFactory<String, String>());
		target = new MinimumEdgesCycleCut<String, String>(source, target).cutCycles();
		
		assertTrue(source.edgeSet().contains("a-->b:ab1"));
		assertFalse(target.edgeSet().contains("a-->b:ab1"));
		assertTrue(source.edgeSet().contains("e-->b:eb1"));
		assertFalse(target.edgeSet().contains("e-->b:eb1"));
	}
}