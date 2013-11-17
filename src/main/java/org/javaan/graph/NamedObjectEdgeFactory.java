package org.javaan.graph;

import org.javaan.model.NamedObject;
import org.jgrapht.EdgeFactory;

/**
 * Implementation of the {@link EdgeFactory} for {@link NamedObject} as vertex type.
 * Edges are of type String and produced by concatenation of source and target name.
 */
public class NamedObjectEdgeFactory implements EdgeFactory<NamedObject, String> {

	@Override
	public String createEdge(NamedObject sourceVertex, NamedObject targetVertex) {
		return sourceVertex.getName() + " -> " + targetVertex.getName();
	}

}
