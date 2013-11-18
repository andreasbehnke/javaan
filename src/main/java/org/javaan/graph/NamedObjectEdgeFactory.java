package org.javaan.graph;

import org.javaan.model.NamedObject;
import org.jgrapht.EdgeFactory;

/**
 * Implementation of the {@link EdgeFactory} for {@link NamedObject} as vertex type.
 * Edges are of type NamedObjectEdge and the identifier is produced by concatenation 
 * of source and target name.
 */
public class NamedObjectEdgeFactory implements EdgeFactory<NamedObject, NamedObjectEdge> {

	@Override
	public NamedObjectEdge createEdge(NamedObject sourceVertex, NamedObject targetVertex) {
		return new NamedObjectEdge(sourceVertex, targetVertex);
	}

}
