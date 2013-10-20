package org.javaan.graph;

import java.util.List;
import java.util.Set;

/**
 * Represents a directed graph with single childs
 *
 * @param <N> type of graph nodes
 */
public interface SingleChildGraph<N> extends Graph<N> {

	N getChild(N parent);
	
	Set<N> getParents(N child);

	boolean hasChild(N parent);

	List<N> getPath(N node);

}