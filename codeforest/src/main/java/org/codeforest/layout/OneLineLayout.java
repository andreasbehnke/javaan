package org.codeforest.layout;

import javax.vecmath.Vector3d;

/**
 * Places vertices along one line
 */
public class OneLineLayout<V> implements Layout<V> {
	
	private final VertexSceneContext<V> context;
	
	private final double vertexWidth;
	
	private final double z;
	
	private final double vertexSpace;

	private double x;
	
	public OneLineLayout(VertexSceneContext<V> context, double vertexWidth, double z, double vertexSpace) {
		this.context = context;
		this.vertexWidth = vertexWidth;
		this.z = z;
		this.vertexSpace = vertexSpace;
	}

	private double getVertexWidth(V vertex) {
		return context.get(vertex).getSubTreeWidth() * vertexWidth;
	}

	@Override
	public void start() {
		x = 0;
	}
	
	@Override
	public Vector3d getPosition(V vertex) {
		double halfVertexWidth = getVertexWidth(vertex) * 0.5d;
		x += halfVertexWidth;
		Vector3d vector3d = new Vector3d(new double[]{x, 0, z});
		x += halfVertexWidth + vertexSpace;
		return vector3d;
	}
}