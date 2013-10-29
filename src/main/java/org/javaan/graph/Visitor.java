package org.javaan.graph;

/**
 * Visits nodes during graph traversal
 */
public interface Visitor<N> {

	void visit(N node, int level);

}
