package org.javaan.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.GraphDelegator;

import java.util.List;

public class AddVerticesAutomatically<V, E> extends GraphDelegator<V, E> {

	private static final long serialVersionUID = 1L;

	public AddVerticesAutomatically(Graph<V, E> g) {
		super(g);
	}

	/**
	 * Add sourceVertex and targetVertex to graph and create an edge between them.
	 */
	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		addVertex(sourceVertex);
		addVertex(targetVertex);
		return super.addEdge(sourceVertex, targetVertex);
	}

	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E edge) {
		addVertex(sourceVertex);
		addVertex(targetVertex);
		return super.addEdge(sourceVertex, targetVertex, edge);
	}

	public void addEdges(V sourceVertex, List<V> targetVertices) {
		addVertex(sourceVertex);
		for (V targetVertex: targetVertices) {
			addVertex(targetVertex);
			super.addEdge(sourceVertex, targetVertex);
		}
	}
}
