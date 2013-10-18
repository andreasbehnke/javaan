package org.javaan.graph;

import java.util.List;

/**
 * Represents a directed graph with single childs
 *
 * @param <N> type of graph nodes
 */
public interface SingleChildGraph<N> extends Graph<N> {

	N getChild(N parent);

	boolean hasChild(N parent);

	List<N> getPath(N node);

}