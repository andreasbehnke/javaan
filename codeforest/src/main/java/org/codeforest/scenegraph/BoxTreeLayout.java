package org.codeforest.scenegraph;

import javax.vecmath.Vector3d;

import org.codeforest.model.VertexSceneContext;

public class BoxTreeLayout<V> implements GraphLayout<V> {

	private final VertexSceneContext<V> context;
	
	private final double vertexWidth;
	
	private final double vertexHeight;

	private double x;
	
	public BoxTreeLayout(VertexSceneContext<V> context, double vertexWidth, double vertexHeight) {
		this.context = context;
		this.vertexWidth = vertexWidth;
		this.vertexHeight = vertexHeight;
	}

	private double getVertexWidth(V vertex) {
		return context.get(vertex).getSubTreeWidth() * vertexWidth;
	}

	public Vector3d startVertex(V sourceVertex) {
		x = - 0.5 * getVertexWidth(sourceVertex);
		return new Vector3d();
	}
	
	public Vector3d processTargetVertex(V targetVertex) {
		double halfTargetWidth = getVertexWidth(targetVertex) * 0.5d;
		x += halfTargetWidth;
		Vector3d vector = new Vector3d(x, vertexHeight, 0);
		x += halfTargetWidth;
		return vector;
	}
}
