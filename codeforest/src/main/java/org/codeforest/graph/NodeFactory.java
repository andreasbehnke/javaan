package org.codeforest.graph;

import javax.media.j3d.Node;

/**
 * Creates node objects for vertices
 */
public interface NodeFactory<V> {

	Node createNode(V vertex);
}
