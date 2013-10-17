package org.javaan;

import java.util.Set;

/**
 * Represents a digraph without multi-edges.  
 * Provides methods for querying graph.
 * 
 * @param <N> type of graph nodes
 */
public interface Graph<N> {

	/**
	 * Adds node if not exists
	 */
	void addNode(N node);

	void addEdge(N parent, N child);
	
	Set<N> getNodes();

	Set<N> getChilds(N parent);

	boolean hasChilds(N parent);

	boolean containsNode(N node);

	Set<N> getLeaveNodes(N node);

	
}