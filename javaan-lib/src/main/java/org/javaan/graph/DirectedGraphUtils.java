package org.javaan.graph;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.DirectedGraph;

public class DirectedGraphUtils {
	
	private DirectedGraphUtils() {}
	
	private static <V,E> Set<V> getTargetSet(Set<E> edges, DirectedGraph<V, E> graph) {
		Set<V> targets = new HashSet<V>();
		for (E e : edges) {
			targets.add(graph.getEdgeTarget(e));
		}
		return targets;
	}

	private static <V,E> Set<V> getSourceSet(Set<E> edges, DirectedGraph<V, E> graph) {
		Set<V> sources = new HashSet<V>();
		for (E e : edges) {
			sources.add(graph.getEdgeSource(e));
		}
		return sources;
	}

	public static <V,E> Set<V> targetVerticesOf(V vertex, DirectedGraph<V, E> graph) {
		return getTargetSet(graph.outgoingEdgesOf(vertex), graph);
	}

	public static <V,E> Set<V> sourceVerticesOf(V vertex, DirectedGraph<V, E> graph) {
		return getSourceSet(graph.incomingEdgesOf(vertex), graph);
	}
}
