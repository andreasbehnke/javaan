package org.javaan.model;

import java.util.List;

/**
 * A tree is a directed graph with in degree of 1
 */
public interface TreeView<V, E> extends GraphView<V, E> {

	V sourceVertexOf(V vertex);
	
	List<V> predecessorPathOf(V vertex);
}
