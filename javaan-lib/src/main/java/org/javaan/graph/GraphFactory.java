package org.javaan.graph;

import org.javaan.model.Dependency;
import org.javaan.model.GraphView;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;

import java.util.Set;
import java.util.function.Supplier;

public final class GraphFactory {

	private GraphFactory() {}

	public static <V> ExtendedGraph<V, Dependency> createDependencyGraph() {
		return new ExtendedGraph<>(
				new DefaultDirectedGraph<V, Dependency>(null, null, true){
					
					private static final long serialVersionUID = 1L;

					@Override
					public double getEdgeWeight(Dependency e) {
						return e.getCallees().size();
					}
				});
	}

	public static <V, E> ExtendedGraph<V, E> createVertexEdgeGraph(Supplier<E> edgeSupplier) {
		return new ExtendedGraph<>(
				new DefaultDirectedGraph<>(null, edgeSupplier, false)
		);
	}
	
	public static <V, E> Tree<V, E> createVertexEdgeTree(Supplier<E> edgeSupplier) {
		return new Tree<>(
				new DefaultDirectedGraph<>(null, edgeSupplier, false)
		);
	}
	
	public static <V, E> GraphView<V, E> createSubgraphView(ExtendedGraph<V, E> graph, Set<V> vertices, boolean reversed) {
		if (vertices == null && !reversed) {
			return graph;
		}
		if (reversed) {
			return new ExtendedGraph<>(
					new EdgeReversedGraph<>(new AsSubgraph<>(graph.getDelegate(), vertices, null))
					);	
		}
		return new ExtendedGraph<>(
				new AsSubgraph<>(graph.getDelegate(), vertices, null)
				);
	}
}