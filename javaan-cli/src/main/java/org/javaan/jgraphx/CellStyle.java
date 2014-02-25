package org.javaan.jgraphx;

import org.javaan.model.GraphView;

public interface CellStyle<V, E> {
	
	String getEdgeLabel(GraphView<V, E> g, E edge);
	
	String getEdgeStyle(GraphView<V, E> g, E value);
	
	String getVertexLabel(GraphView<V, E> g, V vertex);
	
	String getVertexStyle(GraphView<V, E> g, V value);
}