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
 * Adapter does not support level information for the {@link NamedObjectVisitor}.visit method and
 * will always provide level = -1. 
 */
public class NamedObjectBreadthFirstTraversalListener<V extends NamedObject> extends TraversalListenerAdapter<V, NamedObjectEdge<V>> {

	private static final Logger LOG = LoggerFactory.getLogger(NamedObjectBreadthFirstTraversalListener.class);

	private final NamedObjectVisitor<V> visitor;

	public NamedObjectBreadthFirstTraversalListener(NamedObjectVisitor<V> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<V> e) {
		V vertex = e.getVertex();
		visitor.visit(vertex, -1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited vertex {}", vertex);
		}
	}
}
