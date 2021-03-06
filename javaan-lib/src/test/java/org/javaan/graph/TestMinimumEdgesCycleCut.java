package org.javaan.graph;

import org.javaan.graph.SimpleGraphReader.ObjectProducer;
import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.Assert.*;

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
			+ "E e b eb1 \n"
			+ "E x y xy1 \n" // disconnected parts
			+ "E y x yx1 \n"
			+ "E y x yx2 \n"
			+ "V s \n"
			+ "V t \n"
			+ "E x v xv \n"
			+ "E v v vv1 \n" // self-cycle
			+ "E v v vv2 \n";

	private DirectedPseudograph<String, String> createGraph() throws IOException {
		DirectedPseudograph<String, String> graph = new DirectedPseudograph<>(null, null, false);
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
		new SimpleGraphReader<>(graph, objectProducer).readGraph(new StringReader(GRAPH_CONTENT));
		return graph;
	}

	@Test
	public void testFindCutPoints() throws IOException {
		DirectedMultigraph<String, String> target = new DirectedMultigraph<>(null, null, false);
		List<CutPoint<String, String>> cutPoints = new MinimumEdgesCycleCut<>(createGraph(), target).findCutPoints();
		assertNotNull(cutPoints);
		assertEquals(4, cutPoints.size());
		assertTrue(cutPoints.contains(new CutPoint<String, String>("a", "b")));
		assertTrue(cutPoints.contains(new CutPoint<String, String>("e", "b")));
		assertTrue(cutPoints.contains(new CutPoint<String, String>("x", "y")));
		assertTrue(cutPoints.contains(new CutPoint<String, String>("v", "v")));
	}
	
	@Test
	public void testCutCycles() throws IOException {
		DirectedPseudograph<String, String> source = createGraph();
		Graph<String, String> target = new DirectedMultigraph<>(null, null, false);
		target = new MinimumEdgesCycleCut<>(source, target).cutCycles();
		
		assertTrue(source.edgeSet().contains("a-->b:ab1"));
		assertFalse(target.edgeSet().contains("a-->b:ab1"));

		assertTrue(source.edgeSet().contains("e-->b:eb1"));
		assertFalse(target.edgeSet().contains("e-->b:eb1"));
		
		assertTrue(source.edgeSet().contains("x-->y:xy1"));
		assertFalse(target.edgeSet().contains("x-->y:xy1"));

		assertTrue(source.edgeSet().contains("v-->v:vv1"));
		assertTrue(source.edgeSet().contains("v-->v:vv2"));
		assertFalse(target.edgeSet().contains("v-->v:vv1"));
		assertFalse(target.edgeSet().contains("v-->v:vv2"));
	}
	
	@Test
	public void testCutCyclesCompleteGraph() {
		CompleteGraphGenerator<String, String> generator = new CompleteGraphGenerator<>(5);
		Supplier<String> vertexSupplier = new Supplier<String>() {
			@Override
			public String get() {
				return UUID.randomUUID().toString();
			}
		};
		Graph<String, String> graph = new DefaultDirectedGraph<String, String>(vertexSupplier, null, false) {
			@Override
			public String addEdge(String sourceVertex, String targetVertex) {
				String edge = sourceVertex + targetVertex;
				addEdge(sourceVertex, targetVertex, edge);
				return edge;
			}
		};
		generator.generateGraph(graph, new HashMap<>());
		Graph<String, String> target = new DefaultDirectedGraph<>(null, null, false);
		new MinimumEdgesCycleCut<>(graph, target).cutCycles();
	}
}