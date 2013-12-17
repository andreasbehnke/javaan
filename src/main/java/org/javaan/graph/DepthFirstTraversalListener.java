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

import java.util.Set;
import java.util.Stack;

import org.jgrapht.Graph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter between the simpler {@link ObjectVisitor} interface and the more
 * specific {@link TraversalListener} interface from the jgrapht library. This keeps
 * the code clean from intrusive dependencies on jgrapht library.
 * Supports level information for the {@link ObjectVisitor}.visit method.
 */
class DepthFirstTraversalListener<V, E> extends TraversalListenerAdapter<V, E> {

	private static final Logger LOG = LoggerFactory.getLogger(DepthFirstTraversalListener.class);
	
	private final Graph<V, E> graph;
	
	private final ObjectVisitor<V, E> visitor;
	
	private final Stack<V> stack = new Stack<V>();
	
	public DepthFirstTraversalListener(Graph<V, E> graph, ObjectVisitor<V, E> visitor) {
		this.graph = graph;
		this.visitor = visitor;
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		stack.pop();
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		int level = stack.size();
		if (!stack.empty()) {
			V predecessor = stack.peek();
			Set<E> edges = graph.getAllEdges(predecessor, vertex);
			for (E edge : edges) {
				visitor.visitEdge(edge, level);
				//if (LOG.isDebugEnabled()) {
				LOG.info("Visited edge {} at level {}", edge, level);
				//}
			}
		}
		visitor.visitVertex(vertex, level);
		stack.push(vertex);
		//if (LOG.isDebugEnabled()) {
			LOG.info("Visited vertex {} at level {}", vertex, level);
		//}
	}
}
