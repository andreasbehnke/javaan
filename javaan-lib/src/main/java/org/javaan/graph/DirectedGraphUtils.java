package org.javaan.graph;

import org.jgrapht.Graph;

import java.util.HashSet;
import java.util.Set;

public class DirectedGraphUtils {
	
	private DirectedGraphUtils() {}
	
	private static <V,E> Set<V> getTargetSet(Set<E> edges, Graph<V, E> graph) {
		Set<V> targets = new HashSet<>();
		for (E e : edges) {
			targets.add(graph.getEdgeTarget(e));
		}
		return targets;
	}

	private static <V,E> Set<V> getSourceSet(Set<E> edges, Graph<V, E> graph) {
		Set<V> sources = new HashSet<>();
		for (E e : edges) {
			sources.add(graph.getEdgeSource(e));
		}
		return sources;
	}

	public static <V,E> Set<V> targetVerticesOf(V vertex, Graph<V, E> graph) {
		return getTargetSet(graph.outgoingEdgesOf(vertex), graph);
	}

	public static <V,E> Set<V> sourceVerticesOf(V vertex, Graph<V, E> graph) {
		return getSourceSet(graph.incomingEdgesOf(vertex), graph);
	}
}
