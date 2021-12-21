package org.javaan.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.javaan.graph.SimpleGraphReader.ObjectProducer;
import org.jgrapht.graph.DirectedMultigraph;
import org.junit.Test;

public class TestTopologicalMultiGraphSort {

	private static final String GRAPH_CONTENT =
			"E a b ab1 \n"
			+ "E a b ab2 \n"
			+ "E b c bc1 \n"
			+ "E b c bc2 \n"
			+ "E c d cd1 \n"
			+ "E c d cd2 \n"
			+ "E c e ce1 \n"
			+ "E c e ce2 \n"
			+ "E c e ce3 \n"
			+ "E f g fg1 \n"
			+ "E g b gb1 \n";

	private DirectedMultigraph<String, String> createGraph() throws IOException {
		DirectedMultigraph<String, String> graph = new DirectedMultigraph<>(null, null, false);
		ObjectProducer<String, String> objectProducer = new ObjectProducer<>() {
			@Override
			public String createEdge(String source, String target, String edgeLabel) {
				return source + "-->" + target + ":" + edgeLabel;
			}

			@Override
			public String createVertex(String vertexLabel) {
				return vertexLabel;
			}
		};
		new SimpleGraphReader<>(graph, objectProducer).readGraph(new StringReader(GRAPH_CONTENT));
		return graph;
	}

	@Test
	public void testSort() throws IOException {
		DirectedMultigraph<String, String> graph = createGraph();
		List<String> sorted = new TopologicalMultiGraphSort<>(graph).sort();
		assertNotNull(sorted);
		assertEquals(graph.vertexSet().size(), sorted.size());
		assertEquals("f", sorted.get(0));
		assertEquals("g", sorted.get(1));
		assertEquals("a", sorted.get(2));
		assertEquals("b", sorted.get(3));
		assertEquals("c", sorted.get(4));
		assertEquals("e", sorted.get(5));
		assertEquals("d", sorted.get(6));
	}
}
