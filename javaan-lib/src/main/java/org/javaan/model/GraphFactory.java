package org.javaan.model;

import org.javaan.graph.TraversalDirectedGraph;
import org.javaan.graph.UnsupportedEdgeFactory;
import org.javaan.graph.VertexEdge;
import org.javaan.graph.VertexEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

public final class GraphFactory {

	private GraphFactory() {}

	public static <V> TraversalDirectedGraph<V, Dependency> createDependencyGraph() {
		return new TraversalDirectedGraph<V, Dependency>(
				new DefaultDirectedGraph<V, Dependency>(
						new UnsupportedEdgeFactory<V, Dependency>())) {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public double getEdgeWeight(Dependency e) {
				return e.getCallees().size();
			}
		};
	}
	
	public static <V> TraversalDirectedGraph<V, VertexEdge<V>> createVertexEdgeDirectedGraph() {
		return new TraversalDirectedGraph<>(
				new DefaultDirectedGraph<V, VertexEdge<V>>(
						new VertexEdgeFactory<V>())
		);
	}
}