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

import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter between the simpler {@link GraphVisitor} interface and the more
 * specific {@link TraversalListener} interface from the jgrapht library. This keeps
 * the code clean from intrusive dependencies on jgrapht library.
 * Breadth first traversal does not support level information for the {@link GraphVisitor}.visit method,
 * level will always be -1 .
 */
class BreadthFirstTraversalListener<V, E> extends TraversalListenerAdapter<V, E> {
	
	private static final Logger LOG = LoggerFactory.getLogger(BreadthFirstTraversalListener.class);
	
	private final GraphVisitor<V, E> visitor;
	
	public BreadthFirstTraversalListener(GraphVisitor<V, E> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		visitor.visitVertex(vertex, -1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited vertex {}", vertex);
		}
	}
	
	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		visitor.vertexFinished(e.getVertex(), -1);
	}
	
	@Override
	public void edgeTraversed(EdgeTraversalEvent<V, E> e) {
		E edge = e.getEdge();
		visitor.visitEdge(edge, -1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited edge {}", edge);
		}
	}
}
