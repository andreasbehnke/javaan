package org.javaan.graph;

import org.javaan.model.Dependency;
import org.javaan.model.GraphView;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;

import java.util.Set;

public final class GraphFactory {

	private GraphFactory() {}

	public static <V> ExtendedGraph<V, Dependency> createDependencyGraph() {
		return new ExtendedGraph<>(
				new DefaultDirectedGraph<V, Dependency>(
						new UnsupportedEdgeFactory<>()){
					
					private static final long serialVersionUID = 1L;

					@Override
					public double getEdgeWeight(Dependency e) {
						return e.getCallees().size();
					}
				});
	}

	public static <V> ExtendedGraph<V, VertexEdge<V>> createVertexEdgeGraph() {
		return new ExtendedGraph<>(
				new DefaultDirectedGraph<>(
						new VertexEdgeFactory<>())
		);
	}
	
	public static <V> Tree<V, VertexEdge<V>> createVertexEdgeTree() {
		return new Tree<>(
				new DefaultDirectedGraph<>(
						new VertexEdgeFactory<>())
		);
	}
	
	public static <V, E> GraphView<V, E> createSubgraphView(ExtendedGraph<V, E> graph, Set<V> vertices, boolean reversed) {
		if (vertices == null && !reversed) {
			return graph;
		}
		if (reversed) {
			return new ExtendedGraph<>(
					new EdgeReversedGraph<>(new DirectedSubgraph<>(graph.getDelegate(), vertices, null))
					);	
		}
		return new ExtendedGraph<>(
				new DirectedSubgraph<>(graph.getDelegate(), vertices, null)
				);
	}
}