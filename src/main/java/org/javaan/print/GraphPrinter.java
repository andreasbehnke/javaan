package org.javaan.print;

import java.io.PrintStream;

import org.javaan.graph.GraphVisitor;
import org.javaan.graph.GraphVisitorAdapter;
import org.jgrapht.Graph;

public class GraphPrinter<V, E> extends GraphVisitorAdapter<V, E> implements GraphVisitor<V, E> {
	
	private static final String DEFAULT_GRAPH_SEPARATOR_FORMAT = "graph %s:";
	
	private final ObjectFormatter<V> vertexFormatter;
	
	private final ObjectFormatter<E> edgeFormatter;
	
	private final PrintStream output;
	
	private final String graphSeparatorFormat;
	
	public GraphPrinter(PrintStream output, ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter) {
		this(output, vertexFormatter, edgeFormatter, DEFAULT_GRAPH_SEPARATOR_FORMAT);
	}
	
	public GraphPrinter(PrintStream output, ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter, String graphSeparatorFormat) {
		this.output = output;
		this.vertexFormatter = vertexFormatter;
		this.edgeFormatter = edgeFormatter;
		this.graphSeparatorFormat = graphSeparatorFormat;
	}
	
	@Override
	public void visitGraph(Graph<V, E> graph, int index) {
		PrintUtil.printSeparator(output);
		output.format(graphSeparatorFormat, index).println();
	}

	@Override
	public void visitVertex(V vertex, int level) {
		PrintUtil.indent(output, vertexFormatter, vertex, level * 2 + 1);
	}

	@Override
	public void visitEdge(E edge, int level) {
		PrintUtil.indent(output, edgeFormatter, edge, level * 2);
	}
}