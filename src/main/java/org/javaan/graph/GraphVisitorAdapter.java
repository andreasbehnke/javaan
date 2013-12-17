package org.javaan.graph;

import org.jgrapht.Graph;

public abstract class GraphVisitorAdapter<V, E> implements GraphVisitor<V, E> {

	@Override
	public boolean finished() { 
		return false;
	}

	@Override
	public void visitGraph(Graph<V, E> graph, int index) {}

	@Override
	public void visitVertex(V vertex, int level) {}

	@Override
	public void visitEdge(E edge, int level) {}

}
