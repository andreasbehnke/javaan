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

import org.javaan.model.NamedObject;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter between the simpler {@link NamedObjectVisitor} interface and the more
 * specific {@link TraversalListener} interface from the jgrapht library. This keeps
 * the code clean from intrusive dependencies on jgrapht library.
 * Adapter for depth first traversal supports level information for the {@link NamedObjectVisitor}.visit method,
 * for breadth first traversal level will always be -1.
 */
class NamedObjectTraversalListener<V extends NamedObject> extends TraversalListenerAdapter<V, NamedObjectEdge<V>> {

	private static final Logger LOG = LoggerFactory.getLogger(NamedObjectTraversalListener.class);
	
	private final NamedObjectVisitor<V> visitor;
	
	private int level = 0;
	
	public NamedObjectTraversalListener(NamedObjectVisitor<V> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<V> e) {
		level --;
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		level ++;
		visitor.visit(vertex, level - 1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited vertex {} at level {}", vertex, level);
		}
	}
	
}
