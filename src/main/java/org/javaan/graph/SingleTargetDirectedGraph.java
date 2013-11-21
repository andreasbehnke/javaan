package org.javaan.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javaan.model.NamedObject;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

/**
 * A directed graph which is constrained to out degree of 1.
 */
public class SingleTargetDirectedGraph<V extends NamedObject> extends NamedObjectDirectedGraph<V> {

	private static final long serialVersionUID = 1L;

	public NamedObjectEdge<V> addEdge(V sourceVertex, V targetVertex) {
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
		GraphIterator<V, NamedObjectEdge<V>> iterator = new DepthFirstIterator<V, NamedObjectEdge<V>>(this, vertex);
		while(iterator.hasNext()) {
			path.add(iterator.next());
		}
		return path;
	}

}
