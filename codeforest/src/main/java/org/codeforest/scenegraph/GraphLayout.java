package org.codeforest.scenegraph;

import javax.vecmath.Vector3d;

/**
 * Defines a layout algorithm for the position of
 * directed graph's vertices
 */
public interface GraphLayout<V> {

	Vector3d startVertex(V sourceVertex);
	
	Vector3d processTargetVertex(V targetVertex);
}
