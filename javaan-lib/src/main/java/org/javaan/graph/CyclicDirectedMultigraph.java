package org.javaan.graph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.AbstractBaseGraph;

/**
 * Represents a directed multigraph which supports cycles.
 */
public class CyclicDirectedMultigraph<V, E> extends AbstractBaseGraph<V, E> implements DirectedGraph<V, E> {

	private static final long serialVersionUID = 0L;

	public CyclicDirectedMultigraph(EdgeFactory<V, E> ef) {
		super(ef, true, true);
	}	
}