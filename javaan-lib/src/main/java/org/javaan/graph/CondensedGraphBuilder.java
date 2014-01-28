package org.javaan.graph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedPseudograph;

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
		final TraversalDirectedGraph<V, CondensedEdge<V, E>> condensedGraph = new TraversalDirectedGraph<V, CondensedEdge<V, E>>(
				new DirectedPseudograph<V, CondensedEdge<V, E>>(
						new UnsupportedEdgeFactory<V, CondensedEdge<V, E>>()));
		graph.traverseDepthFirst(new GraphVisitorAdapter<V, E>() {
			@Override
			public void visitEdge(E edge, int level) {
				V target = graph.getEdgeTarget(edge);
				V source = graph.getEdgeSource(edge);
				CondensedEdge<V, E> condensedEdge = condensedGraph.getEdge(source, target);
				if (condensedEdge == null) {
					condensedEdge = new CondensedEdge<V, E>(source, target);
					condensedGraph.addEdge(source, target, condensedEdge);
				}
				condensedEdge.addEdge(edge);
			}
		});
		return condensedGraph;
	}
}
