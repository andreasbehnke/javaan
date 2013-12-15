package org.javaan.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * Directed graph implementation which does not support edge factory,
 * the edge instance must be provided from outside.
 */
public class ExternalEdgeDirectedGraph<V, E> extends TraversalDirectedGraph<V, E> {

	private static final long serialVersionUID = 1L;

	public ExternalEdgeDirectedGraph() {
		super(new DefaultDirectedGraph<V, E>((EdgeFactory<V, E>)null));
	}
	
	@Override
	public E addEdge(V source, V target) {
		throw new UnsupportedOperationException();
	}
}
