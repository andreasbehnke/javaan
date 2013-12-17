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
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

/**
 * A directed graph which is constrained to out degree of 1.
 */
public class SingleTargetDirectedGraph<V extends NamedObject> extends VertexEdgeDirectedGraph<V> {

	private static final long serialVersionUID = 1L;

	public VertexEdge<V> addEdge(V sourceVertex, V targetVertex) {
		if (containsVertex(sourceVertex)  && outDegreeOf(sourceVertex) > 0) {
			throw new IllegalArgumentException("source vertex " + sourceVertex + " already contains a target vertex!");
		}
		return super.addEdge(sourceVertex, targetVertex);
	}

	public V targetVertexOf(V sourceVertex) {
		Set<V> targets = targetVerticesOf(sourceVertex);
		if (targets == null || targets.size() == 0) {
			return null;
		}
		return targets.iterator().next();
	}

	public List<V> successorPathOf(V vertex) {
		List<V> path = new ArrayList<V>();
		GraphIterator<V, VertexEdge<V>> iterator = new DepthFirstIterator<V, VertexEdge<V>>(this, vertex);
		while(iterator.hasNext()) {
			path.add(iterator.next());
		}
		return path;
	}

}
