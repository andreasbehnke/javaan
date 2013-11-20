package org.javaan.graph;

import org.javaan.model.NamedObject;

/**
 * Visits named objects during traversal of {@link NamedObjectDirectedGraph}
 */
public interface NamedObjectVisitor<N extends NamedObject> {

	void visit(N named, int level);
}
