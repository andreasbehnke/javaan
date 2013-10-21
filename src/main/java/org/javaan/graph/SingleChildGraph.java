package org.javaan.graph;

import java.util.List;

/**
 * Represents a directed graph with single child
 *
 * @param <N> type of graph nodes
 */
public interface SingleChildGraph<N> extends Digraph<N> {

	N getChild(N parent);

	List<N> getPath(N node);

}