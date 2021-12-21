package org.javaan.graph;

import org.javaan.graph.SimpleGraphReader.ObjectProducer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class TestSimpleGraphReader {

	private static final String EDGE_CREATED_BY_INTERNAL_FACTORY = "anotherVertex-->firstVertex:edgeCreatedByInternalFactory";

	private static final String SECOND_EDGE = "anotherVertex-->secondVertex:secondEdge";

	private static final String FIRST_EDGE = "firstVertex-->anotherVertex:firstEdge";

	private static final String ANOTHER_VERTEX = "anotherVertex";

	private static final String SECOND_VERTEX = "secondVertex";

	private static final String FIRST_VERTEX = "firstVertex";

	private static final String GRAPH_CONTENT = "# just a comment\n"
			+ " # another comment\n\t   \n"
			+ "\tV firstVertex\t\n"
			+ "v secondVertex\n"
			+ "E firstVertex anotherVertex firstEdge\n"
			+ "E anotherVertex secondVertex secondEdge\n"
			+ "E anotherVertex firstVertex \t\n\n";

	@Test
	public void testReadGraph() throws IOException {
		Graph<String, String> target = new DefaultDirectedGraph<>(null, null, false) {
			@Override
			public String addEdge(String sourceVertex, String targetVertex) {
				String edge = sourceVertex + "-->" + targetVertex + ":edgeCreatedByInternalFactory";
				super.addEdge(sourceVertex, targetVertex, edge);
				return edge;
			}
		};
		SimpleGraphReader<String, String> graphReader = new SimpleGraphReader<>(target, new ObjectProducer<>() {

			@Override
			public String createVertex(String vertexLabel) {
				return vertexLabel;
			}

			@Override
			public String createEdge(String source, String target, String edgeLabel) {
				return source + "-->" + target + ":" + edgeLabel;
			}

		});

		target = graphReader.readGraph(new StringReader(GRAPH_CONTENT));

		assertNotNull(target);

		assertTrue(target.vertexSet().contains(FIRST_VERTEX));
		assertTrue(target.vertexSet().contains(SECOND_VERTEX));
		assertTrue(target.vertexSet().contains(ANOTHER_VERTEX));
		assertEquals(FIRST_VERTEX, graphReader.getVertexMap().get(FIRST_VERTEX));
		assertEquals(SECOND_VERTEX, graphReader.getVertexMap().get(SECOND_VERTEX));
		assertEquals(ANOTHER_VERTEX, graphReader.getVertexMap().get(ANOTHER_VERTEX));

		assertTrue(target.edgeSet().contains(FIRST_EDGE));
		assertEquals(FIRST_EDGE, graphReader.getEdgeMap().get(FIRST_EDGE));
		assertTrue(target.edgeSet().contains(SECOND_EDGE));
		assertEquals(SECOND_EDGE, graphReader.getEdgeMap().get(SECOND_EDGE));
		assertTrue(target.edgeSet().contains(EDGE_CREATED_BY_INTERNAL_FACTORY));
		assertEquals(EDGE_CREATED_BY_INTERNAL_FACTORY, graphReader.getEdgeMap().get(EDGE_CREATED_BY_INTERNAL_FACTORY));

		assertEquals(FIRST_VERTEX, target.getEdgeSource(FIRST_EDGE));
		assertEquals(ANOTHER_VERTEX, target.getEdgeTarget(FIRST_EDGE));
		assertEquals(ANOTHER_VERTEX, target.getEdgeSource(EDGE_CREATED_BY_INTERNAL_FACTORY));
		assertEquals(FIRST_VERTEX, target.getEdgeTarget(EDGE_CREATED_BY_INTERNAL_FACTORY));
	}

}
