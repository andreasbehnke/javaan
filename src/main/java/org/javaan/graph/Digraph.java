package org.javaan.graph;

import java.util.Set;

/**
 * Represents a digraph without multi-edges.  
 * Provides methods for querying graph.
 * 
 * @param <N> type of graph nodes
 */
public interface Digraph<N> extends Graph<N> {

	Set<N> getChilds(N parent);
	
	Set<N> getParents(N child);
	
	Set<N> getSuccessors(N parent);
	
	Set<N> getPredecessors(N child);

	boolean hasChilds(N parent);

	Set<N> getLeaveNodes(N node);
}