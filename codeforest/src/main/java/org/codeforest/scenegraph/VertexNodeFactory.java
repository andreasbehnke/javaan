package org.codeforest.scenegraph;

import javax.media.j3d.Node;

/**
 * Creates 3d scene graph nodes for vertices
 */
public interface VertexNodeFactory<V> {
	Node createNode(V vertex);
}
