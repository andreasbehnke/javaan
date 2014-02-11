package org.javaan.graph;

import java.util.Set;

import org.jgrapht.Graph;

/**
 * Creates a subgraph of source graph by applying a vertex filter.
 */
public class GraphFilter<E, V> {

	private final Graph<V, E> source;
	
	private final Graph<V, E> target;

	public GraphFilter(Graph<V, E> source, Graph<V, E> target) {
		this.source = source;
		this.target = target;
	}
	
	public Graph<V, E> filter(Set<V> vertexFilter) {
		for (V v : source.vertexSet()) {
			if (vertexFilter.contains(v)) {
				target.addVertex(v);
			}
		}
		for (E e : source.edgeSet()) {
			V sv = source.getEdgeSource(e);
			V tv = source.getEdgeTarget(e);
			if (target.containsVertex(sv) && target.containsVertex(tv)) {
				target.addEdge(sv, tv, e);
			}
		}
		return target;
	}
}
