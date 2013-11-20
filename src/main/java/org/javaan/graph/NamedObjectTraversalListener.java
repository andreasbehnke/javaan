package org.javaan.graph;

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
