package org.codeforest.scenegraph;

import javax.vecmath.Vector3d;

/**
 * Defines a layout algorithm for positioning vertices.
 */
public interface Layout<V> {
	
	void start();

	Vector3d getPosition(V vertex);

}
