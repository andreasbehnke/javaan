package org.codeforest.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to scene information for all vertices of the displayed graph.
 * This class is not thread safe.
 */
public class VertexSceneContext<V> {

	private final Map<V, SceneData> vertexSceneData = new HashMap<V, SceneData>();
	
	/**
	 * Retrieve scene data for given vertex. 
	 * Scene data will be lazy initialized, if it does not exists.
	 */
	public SceneData get(V vertex) {
		SceneData data = vertexSceneData.get(vertex);
		if (data == null) {
			data = new SceneData();
			vertexSceneData.put(vertex, data);
		}
		return data;
	}
}
