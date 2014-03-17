package org.javaan.graph;

import java.util.Set;

import org.javaan.model.Dependency;
import org.javaan.model.GraphView;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;

public final class GraphFactory {

	private GraphFactory() {}

	public static <V> ExtendedDirectedGraph<V, Dependency> createDependencyGraph() {
		return new ExtendedDirectedGraph<V, Dependency>(
				new DefaultDirectedGraph<V, Dependency>(
						new UnsupportedEdgeFactory<V, Dependency>()){
					
					private static final long serialVersionUID = 1L;

					@Override
					public double getEdgeWeight(Dependency e) {
						return e.getCallees().size();
					}
				});
	}

	public static <V> ExtendedDirectedGraph<V, VertexEdge<V>> createVertexEdgeDirectedGraph() {
		return new ExtendedDirectedGraph<>(
				new DefaultDirectedGraph<V, VertexEdge<V>>(
						new VertexEdgeFactory<V>())
		);
	}
	
	public static <V> Tree<V, VertexEdge<V>> createVertexEdgeTree() {
		return new Tree<>(
				new DefaultDirectedGraph<V, VertexEdge<V>>(
						new VertexEdgeFactory<V>())
		);
	}
	
	public static <V, E> GraphView<V, E> createSubgraphView(ExtendedDirectedGraph<V, E> graph, Set<V> vertices, boolean reversed) {
		if (vertices == null && reversed == false) {
			return graph;
		}
		if (reversed) {
			return new ExtendedDirectedGraph<V, E>(
					new EdgeReversedGraph<V, E>(new DirectedSubgraph<V, E>(graph.getDelegate(), vertices, null))
					);	
		}
		return new ExtendedDirectedGraph<V, E>(
				new DirectedSubgraph<V, E>(graph.getDelegate(), vertices, null)
				);
	}
}