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

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.javaan.model.GraphVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;

/**
 * Adapter between the simpler {@link GraphVisitor} interface and the more
 * specific {@link TraversalListener} interface from the jgrapht library. This keeps
 * the code clean from intrusive dependencies on jgrapht library.
 * Supports level information for the {@link GraphVisitor}.visit method.
 */
class DepthFirstTraversalListener<V, E> extends TraversalListenerAdapter<V, E> {
	
	private final DirectedGraph<V, E> graph;
	
	private final GraphVisitor<V, E> visitor;
	
	private final Stack<V> stack = new Stack<V>();
	
	private final Set<E> visitedEdges = new HashSet<E>();
	
	public DepthFirstTraversalListener(DirectedGraph<V, E> graph, GraphVisitor<V, E> visitor) {
		this.graph = graph;
		this.visitor = visitor;
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		int level = stack.size();
		V vertex = stack.pop();
		Set<E> edges = new HashSet<E>(graph.outgoingEdgesOf(vertex));
		edges.removeAll(visitedEdges);
		for (E edge : edges) {
			visitor.visitEdge(edge, level);
		}
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		int level = stack.size();
		if (!stack.empty()) {
			V predecessor = stack.peek();
			Set<E> connectedEdges = graph.getAllEdges(predecessor, vertex);
			for (E edge : connectedEdges) {
				visitor.visitEdge(edge, level);
			}
			visitedEdges.addAll(connectedEdges);
		}
		visitor.visitVertex(vertex, level);
		stack.push(vertex);
	}
}
