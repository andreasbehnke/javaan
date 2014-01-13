package org.codeforest.graph;

import org.javaan.graph.ExternalEdgeDirectedGraph;
import org.javaan.graph.GraphVisitorAdapter;
import org.javaan.graph.TraversalDirectedGraph;
import org.jgrapht.DirectedGraph;

/**
 * Creates a graph with single edge between two vertices from input multigraph (supports multiple edges).
 * Collects the edge information of the original graph with {@link CondensedEdge}. 
 */
public class CondensedGraphBuilder<V, E> {

	private final TraversalDirectedGraph<V, E> graph;

	public CondensedGraphBuilder(DirectedGraph<V, E> originalGraph) {
		this.graph = new TraversalDirectedGraph<>(originalGraph);
	}
	
	public DirectedGraph<V, CondensedEdge<V, E>> createCondensedGraph() {
		final ExternalEdgeDirectedGraph<V, CondensedEdge<V, E>> condensedGraph = new ExternalEdgeDirectedGraph<>();
		graph.traverseDepthFirst(new GraphVisitorAdapter<V, E>() {
			@Override
			public void visitEdge(E edge, int level) {
				V target = graph.getEdgeTarget(edge);
				V source = graph.getEdgeSource(edge);
				CondensedEdge<V, E> condensedEdge = condensedGraph.getEdge(source, target);
				if (condensedEdge == null) {
					condensedEdge = condensedGraph.getEdge(target, source);
				}
				if (condensedEdge == null) {
					condensedEdge = new CondensedEdge<V, E>(target, source);
					condensedGraph.addEdge(source, target, condensedEdge);
				}
				condensedEdge.addEdge(source, target, edge);
			}
		});
		return condensedGraph;
	}
}
