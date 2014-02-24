package org.javaan.model;

import org.javaan.graph.ExtendedDirectedGraph;
import org.javaan.graph.Tree;
import org.javaan.graph.UnsupportedEdgeFactory;
import org.javaan.graph.VertexEdge;
import org.javaan.graph.VertexEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

public final class GraphFactory {

	private GraphFactory() {}

	public static <V> ExtendedDirectedGraph<V, Dependency> createDependencyGraph() {
		return new ExtendedDirectedGraph<V, Dependency>(
				new DefaultDirectedGraph<V, Dependency>(
						new UnsupportedEdgeFactory<V, Dependency>())) {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public double getEdgeWeight(Dependency e) {
				return e.getCallees().size();
			}
		};
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
}