package org.javaan.graph;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javaan.model.NamedObject;
import org.javaan.model.TreeView;

/**
 * TODO: rename me!
 */
public class SingleTargetDirectedGraph<V extends NamedObject> extends VertexEdgeDirectedGraph<V> implements TreeView<V, VertexEdge<V>> {

	private static final long serialVersionUID = 1L;

	public VertexEdge<V> addEdge(V sourceVertex, V targetVertex) {
		if (containsVertex(targetVertex)  && inDegreeOf(targetVertex) > 0) {
			throw new IllegalArgumentException("target vertex " + targetVertex + " already contains a parent vertex!");
		}
		return super.addEdge(sourceVertex, targetVertex);
	}

	@Override
	public V sourceVertexOf(V vertex) {
		Set<V> sources = sourceVerticesOf(vertex);
		if (sources == null || sources.size() == 0) {
			return null;
		}
		return sources.iterator().next();

	}

	@Override
	public List<V> predecessorPathOf(V vertex) {
		List<V> path = new ArrayList<V>();
		while(vertex != null) {
			if (path.contains(vertex)) {
				break; // cycle detected
			}
			path.add(vertex);
			Set<V> pre = sourceVerticesOf(vertex);
			if (pre != null && pre.size() > 0) {
				vertex = pre.iterator().next();
			} else {
				break; // no more parent vertices
			}
		}
		return path;
	}

}
