package org.javaan.graph;

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
 * Breadth first traversal does not support level information for the {@link ObjectVisitor}.visit method,
 * level will always be -1 .
 */
class BreadthFirstTraversalListener<V, E> extends TraversalListenerAdapter<V, E> {
	
	private static final Logger LOG = LoggerFactory.getLogger(BreadthFirstTraversalListener.class);
	
	private final ObjectVisitor<V, E> visitor;
	
	public BreadthFirstTraversalListener(ObjectVisitor<V, E> visitor) {
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
	public void edgeTraversed(EdgeTraversalEvent<V, E> e) {
		E edge = e.getEdge();
		visitor.visitEdge(edge, -1);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Visited edge {}", edge);
		}
	}
}
