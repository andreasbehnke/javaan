package org.javaan.graph;

import org.jgrapht.EdgeFactory;

/**
 * Factory which will always throw an exception when trying to create an edge
 * because caller is responsible for providing a valid edge instance.
 */
public class UnsupportedEdgeFactory<V, E> implements EdgeFactory<V, E> {

	@Override
	public E createEdge(V sourceVertex, V targetVertex) {
		throw new UnsupportedOperationException("Caller is responsible for providing a valid edge");
	}

}