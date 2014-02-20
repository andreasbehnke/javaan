package org.javaan.graph;

import org.javaan.model.Dependency;
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
	
	public static <V> VertexEdgeDirectedGraph<V> createVertexEdgeDirectedGraph() {
		return new VertexEdgeDirectedGraph<>();
	}
}