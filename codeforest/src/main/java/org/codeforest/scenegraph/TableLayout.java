package org.codeforest.scenegraph;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3d;

import org.codeforest.layout.VertexSceneContext;

public class TableLayout<V> implements Layout<V> {
	
	private final VertexSceneContext<V> context;
	
	private final double vertexWidth;
	
	private final double vertexBreadth;
	
	private final double vertexSpace;
	
	private Map<Integer, OneLineLayout<V>> rows;
	
	public TableLayout(VertexSceneContext<V> context, double vertexWidth, double vertexBreadth, double vertexSpace) {
		super();
		this.context = context;
		this.vertexWidth = vertexWidth;
		this.vertexBreadth = vertexBreadth;
		this.vertexSpace = vertexSpace;
	}

	@Override
	public void start() {
		rows = new HashMap<Integer, OneLineLayout<V>>();
	}
	
	private OneLineLayout<V> getRowLayout(V vertex) {
		int row = context.get(vertex).getRow();
		OneLineLayout<V> layout = rows.get(row);
		if (layout == null) {
			double z = (vertexBreadth + vertexSpace) * -row;
			layout = new OneLineLayout<V>(context, vertexWidth, z, vertexSpace);
			layout.start();
			rows.put(row, layout);
		}
		return layout;
	}

	@Override
	public Vector3d getPosition(V vertex) {
		return getRowLayout(vertex).getPosition(vertex);
	}

}
