package org.javaan.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.AbstractBaseGraph;

/**
 * Represents a directed multigraph which supports cycles.
 */
public class CyclicDirectedMultigraph<V, E> extends AbstractBaseGraph<V, E> implements Graph<V, E> {

	private static final long serialVersionUID = 0L;

	public CyclicDirectedMultigraph(EdgeFactory<V, E> ef) {
		super(ef, true, true, true, false);
	}	
}