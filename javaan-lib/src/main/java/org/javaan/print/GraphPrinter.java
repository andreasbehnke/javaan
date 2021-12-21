package org.javaan.print;

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

import org.javaan.graph.GraphVisitor;
import org.javaan.graph.GraphVisitorAdapter;
import org.jgrapht.Graph;

import java.io.Writer;

public class GraphPrinter<V, E> extends GraphVisitorAdapter<V, E> implements GraphVisitor<V, E> {

	private static final String DEFAULT_GRAPH_SEPARATOR_FORMAT = "graph %s:";

	private final ObjectFormatter<V> vertexFormatter;

	private final ObjectFormatter<E> edgeFormatter;

	private final Writer writer;

	private final String graphSeparatorFormat;

	public GraphPrinter(Writer writer, ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter) {
		this(writer, vertexFormatter, edgeFormatter, DEFAULT_GRAPH_SEPARATOR_FORMAT);
	}

	public GraphPrinter(Writer writer, ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter, String graphSeparatorFormat) {
		this.writer = writer;
		this.vertexFormatter = vertexFormatter;
		this.edgeFormatter = edgeFormatter;
		this.graphSeparatorFormat = graphSeparatorFormat;
	}

	@Override
	public void visitGraph(Graph<V, E> graph, int index) {
		PrintUtil.printSeparator(writer);
		PrintUtil.format(writer, graphSeparatorFormat, index);
		PrintUtil.println(writer);
	}

	@Override
	public void visitVertex(V vertex, int level) {
		PrintUtil.indent(writer, vertexFormatter, vertex, level * 2 + 1);
	}

	@Override
	public void visitEdge(E edge, int level) {
		PrintUtil.indent(writer, edgeFormatter, edge, level * 2);
	}
}
