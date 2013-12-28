package org.javaan.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 * Calculates the width of vertices in a tree using depth first iteration.
 * Leave vertices receive a width of 1, all other vertices the sum of the child widths. 
 */
public class TreeWidthCalculator<V, E> extends TraversalListenerAdapter<V, E> {

	private final Map<V, Integer> widths = new HashMap<V, Integer>();
	
	private final DirectedGraph<V, E> graph;
	
	public TreeWidthCalculator(DirectedGraph<V, E> graph) {
		this.graph = graph;
	}
	
	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		Set<E> outgoingEdges = graph.outgoingEdgesOf(vertex);
		if (outgoingEdges.size() == 0) {
			widths.put(vertex, 1);
		} else {
			int width = 0;
			for (E edge : outgoingEdges) {
				V target = graph.getEdgeTarget(edge);
				width += widths.get(target);
			}
			widths.put(vertex, width);
		}
	}

	public static <V,E> Map<V, Integer> calculateVertexWidth(DirectedGraph<V, E> graph, V startVertex) {
		TreeWidthCalculator<V, E> calculator = new TreeWidthCalculator<V, E>(graph);
		DepthFirstIterator<V, E> iterator = new DepthFirstIterator<V, E>(graph, startVertex);
		iterator.addTraversalListener(calculator);
		while(iterator.hasNext()) {
			iterator.next();
		}
		return calculator.widths;
	}
	
}
