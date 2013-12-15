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
 * Adapter between the simpler {@link ObjectVisitor} interface and the more
 * specific {@link TraversalListener} interface from the jgrapht library. This keeps
 * the code clean from intrusive dependencies on jgrapht library.
 * Adapter for depth first traversal supports level information for the {@link ObjectVisitor}.visit method,
 * for breadth first traversal level will always be -1.
 */
class ObjectTraversalListener<V, E> extends TraversalListenerAdapter<V, E> {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectTraversalListener.class);
	
	private final ObjectVisitor<V, E> visitor;
	
	private final boolean isDepthFirstTraversal;
	
	private int level = 0;
	
	public ObjectTraversalListener(ObjectVisitor<V, E> visitor, boolean isDepthFirstTraversal) {
		this.visitor = visitor;
		this.isDepthFirstTraversal = isDepthFirstTraversal;
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		if(isDepthFirstTraversal) level --;
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		if (isDepthFirstTraversal) level ++;
		visitor.visitVertex(vertex, level - 1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited vertex {} at level {}", vertex, level);
		}
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<V, E> edgeTraversalEvent) {
		visitor.visitEdge(edgeTraversalEvent.getEdge(), level - 1);
	}
}
