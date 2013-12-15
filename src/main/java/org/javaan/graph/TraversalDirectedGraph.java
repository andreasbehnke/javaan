package org.javaan.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.GraphDelegator;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class TraversalDirectedGraph<V, E> extends GraphDelegator<V, E> implements DirectedGraph<V, E> {

	private static final long serialVersionUID = 1L;

	public TraversalDirectedGraph(DirectedGraph<V, E> g) {
		super(g);
	}

	/**
	 * Add sourceVertex and targetVertex to graph and create an edge between them.
	 */
	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		addVertex(sourceVertex);
		addVertex(targetVertex);
		return super.addEdge(sourceVertex, targetVertex);
	}

	private Set<V> getTargetSet(Set<E> edges) {
		Set<V> targets = new HashSet<V>();
		for (E e : edges) {
			targets.add(getEdgeTarget(e));
		}
		return targets;
	}

	private Set<V> getSourceSet(Set<E> edges) {
		Set<V> sources = new HashSet<V>();
		for (E e : edges) {
			sources.add(getEdgeSource(e));
		}
		return sources;
	}

	public Set<V> targetVerticesOf(V vertex) {
		return getTargetSet(outgoingEdgesOf(vertex));
	}

	public Set<V> sourceVerticesOf(V vertex) {
		return getSourceSet(incomingEdgesOf(vertex));
	}

	public Set<V> successorsOf(V vertex) {
		Set<V> successors = new HashSet<V>();
		Stack<V> successorStack = new Stack<V>();
		successorStack.addAll(targetVerticesOf(vertex));
		while(!successorStack.isEmpty()) {
			V successor = successorStack.pop();
			// detect cycle, ignore existing nodes
			if (!successors.contains(successor)) {
				successors.add(successor);
				successorStack.addAll(targetVerticesOf(successor));
			}
		}
		return successors;
	}

	public Set<V> predecessorsOf(V vertex) {
		Set<V> predecessors = new HashSet<V>();
		Stack<V> predecessorStack = new Stack<V>();
		predecessorStack.addAll(sourceVerticesOf(vertex));
		while(!predecessorStack.isEmpty()) {
			V predecessor = predecessorStack.pop();
			// detect cycle, ignore existing nodes
			if (!predecessors.contains(predecessor)) {
				predecessors.add(predecessor);
				predecessorStack.addAll(sourceVerticesOf(predecessor));
			}
		}
		return predecessors;
	}

	private void traverseGraph(DirectedGraph<V, E> graph, V startVertex, ObjectVisitor<V, E> visitor,
			boolean depthFirst) {
				if (!containsVertex(startVertex)) {
					return;
				}
				TraversalListener<V, E> listener = null;
				GraphIterator<V, E> iterator = null;
				if (depthFirst) {
					listener = new ObjectTraversalListener<V, E>(visitor, true);
					iterator = new DepthFirstIterator<V, E>(graph, startVertex);
				} else {
					listener = new ObjectTraversalListener<V, E>(visitor, false);
					iterator = new BreadthFirstIterator<V, E>(graph, startVertex);
				}
				iterator.addTraversalListener(listener);
				while (iterator.hasNext() && !visitor.finished()) {
					iterator.next();
				}
			}

	public void traverseSuccessorsDepthFirst(V startVertex, ObjectVisitor<V, E> visitor) {
		traverseGraph(this, startVertex, visitor, true);
	}

	public void traversePredecessorsDepthFirst(V startVertex, ObjectVisitor<V, E> visitor) {
		DirectedGraph<V, E> graph = new EdgeReversedGraph<V, E>(this);
		traverseGraph(graph, startVertex, visitor, true);
	}

	public void traverseSuccessorsBreadthFirst(V startVertex, ObjectVisitor<V, E> visitor) {
		traverseGraph(this, startVertex, visitor, false);
	}

	public void traversePredecessorsBreadthFirst(V startVertex, ObjectVisitor<V, E> visitor) {
		DirectedGraph<V, E> graph = new EdgeReversedGraph<V, E>(this);
		traverseGraph(graph, startVertex, visitor, false);
	}

	private Set<V> collectLeafVertices(DirectedGraph<V, E> graph, V startVertex) {
		Set<V> leaves = new HashSet<V>();
		if (graph.containsVertex(startVertex)) {
			GraphIterator<V, E> iterator = new DepthFirstIterator<V, E>(graph, startVertex);
			iterator.next();
			while (iterator.hasNext()) {
				V vertex = iterator.next();
				if (graph.outDegreeOf(vertex) == 0) {
					leaves.add(vertex);
				}
			}
		}
		return leaves;
	}

	public Set<V> getLeafSuccessors(V startVertex) {
		return collectLeafVertices(this, startVertex);
	}

	public Set<V> getLeafPredecessors(V startVertex) {
		DirectedGraph<V, E> graph = new EdgeReversedGraph<V, E>(this);
		return collectLeafVertices(graph, startVertex);
	}

}
