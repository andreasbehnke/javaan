package org.codeforest.scenegraph;

import javax.media.j3d.Node;
import javax.vecmath.Vector3d;

/**
 * Creates 3d scene graph nodes for edges
 */
public interface EdgeNodeFactory<V, E> {

	Node createNode(E edge, V source, V target, Vector3d startVector, Vector3d endVector);
}

