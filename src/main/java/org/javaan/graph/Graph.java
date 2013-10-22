package org.javaan.graph;

import java.util.Set;

public interface Graph<N> {

	/**
	 * Adds node if not exists
	 */
	void addNode(N node);

	void addEdge(N parent, N child);

	Set<N> getNodes();

	boolean containsNode(N node);

}