package org.javaan.jgraphx;

import org.jgrapht.Graph;

public interface CellStyle<V, E> {
	
	String getEdgeLabel(Graph<V, E> g, E edge);
	
	String getEdgeStyle(Graph<V, E> g, E value);
	
	String getVertexLabel(Graph<V, E> g, V vertex);
	
	String getVertexStyle(Graph<V, E> g, V value);
}