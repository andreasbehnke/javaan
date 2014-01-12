package org.codeforest.scenegraph;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Vector3d;

public class LineEdgeFactory<V, E> implements EdgeNodeFactory<V, E> {
	
	private final Appearance appearance;
	
	public LineEdgeFactory() {
		appearance = null;
	}
	
	public LineEdgeFactory(Appearance appearance) {
		this.appearance = appearance;
	}

	public Node createNode(E edge, V source, V target, Vector3d startVector, Vector3d endVector) {
		LineArray lines = new LineArray(2, GeometryArray.COORDINATES);
		lines.setCoordinates(0, new double[] {
				startVector.x, startVector.y, startVector.z,
				endVector.x, endVector.y, endVector.z});
		Shape3D shape = new Shape3D(lines, appearance);
		return shape;
	}
}