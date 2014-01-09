package org.codeforest.scenegraph;

import java.util.Set;

import org.codeforest.model.VertexSceneContext;
import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

/**
 * Calculates the width of vertices in a tree using depth first iteration.
 * Leave vertices receive a width of 1, all other vertices the sum of the child widths. 
 */
public class TreeWidthCalculator<V, E> extends TraversalListenerAdapter<V, E> {

	private final VertexSceneContext<V> context;
	
	private final DirectedGraph<V, E> graph;
	
	public TreeWidthCalculator(VertexSceneContext<V> context, DirectedGraph<V, E> graph) {
		this.context = context;
		this.graph = graph;
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		Set<E> outgoingEdges = graph.outgoingEdgesOf(vertex);
		if (outgoingEdges.size() == 0) {
			context.get(vertex).setSubTreeWidth(1);
		} else {
			int width = 0;
			for (E edge : outgoingEdges) {
				V target = graph.getEdgeTarget(edge);
				width += context.get(target).getSubTreeWidth();
			}
			context.get(vertex).setSubTreeWidth(width);
		}
	}

	public void calculateVertexWidth(V startVertex) {
		GraphIterator<V, E> iterator = new DepthFirstIterator<V, E>(graph, startVertex);
		iterator.addTraversalListener(this);
		while(iterator.hasNext()) {
			iterator.next();
		}
	}
	
}
