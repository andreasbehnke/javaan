package org.codeforest.graph;

import javax.media.j3d.Node;
import javax.vecmath.Vector3d;

/**
 * Creates 3d scene graph nodes for vertices and edges
 */
public interface NodeFactory<V, E> {

	Node createNode(V vertex);
	
	Node createNode(E edge, V source, V target, Vector3d startVector, Vector3d endVector);
}
