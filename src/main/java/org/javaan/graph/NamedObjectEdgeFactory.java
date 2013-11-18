package org.javaan.graph;

import org.javaan.model.NamedObject;
import org.jgrapht.EdgeFactory;

/**
 * Implementation of the {@link EdgeFactory} for {@link NamedObject} as vertex type.
 * Edges are of type NamedObjectEdge and the identifier is produced by concatenation 
 * of source and target name.
 */
public class NamedObjectEdgeFactory<V extends NamedObject> implements EdgeFactory<V, NamedObjectEdge<V>> {

	@Override
	public NamedObjectEdge<V> createEdge(V sourceVertex, V targetVertex) {
		return new NamedObjectEdge<V>(sourceVertex, targetVertex);
	}

}
