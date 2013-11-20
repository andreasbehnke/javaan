package org.javaan.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.javaan.model.NamedObject;
import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class NamedObjectDirectedGraph<V extends NamedObject> extends DefaultDirectedGraph<V, NamedObjectEdge<V>> {

	private static final long serialVersionUID = 1L;

	/**
	 * Add sourceVertex and targetVertex to graph and create an edge between them.
	 */
	@Override
	public NamedObjectEdge<V> addEdge(V sourceVertex, V targetVertex) {
		addVertex(sourceVertex);
		addVertex(targetVertex);
		return super.addEdge(sourceVertex, targetVertex);
	}

	public NamedObjectDirectedGraph() {
		super(new NamedObjectEdgeFactory<V>());
	}

	public Set<V> targetVerticesOf(V vertex) {
		return NamedObjectEdge.getTargetSet(outgoingEdgesOf(vertex));
	}

	public Set<V> sourceVerticesOf(V vertex) {
		return NamedObjectEdge.getSourceSet(incomingEdgesOf(vertex));
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

	private void traverseGraph(DirectedGraph<V, NamedObjectEdge<V>> graph, V startVertex, NamedObjectVisitor<V> visitor) {
		TraversalListener<V, NamedObjectEdge<V>> listener = new NamedObjectTraversalListener<V>(visitor);
		GraphIterator<V, NamedObjectEdge<V>> iterator = new DepthFirstIterator<V, NamedObjectEdge<V>>(graph, startVertex);
		iterator.addTraversalListener(listener);
		while (iterator.hasNext()) {
			iterator.next();
		}
	}

	public void traverseSuccessorsDepthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		traverseGraph(this, startVertex, visitor);
	}

	public void traversePredecessorsDepthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		DirectedGraph<V, NamedObjectEdge<V>> graph = new EdgeReversedGraph<V, NamedObjectEdge<V>>(this);
		traverseGraph(graph, startVertex, visitor);
	}

	private Set<V> collectLeafVertices(DirectedGraph<V, NamedObjectEdge<V>> graph, V startVertex) {
		Set<V> leaves = new HashSet<V>();
		GraphIterator<V, NamedObjectEdge<V>> iterator = new DepthFirstIterator<V, NamedObjectEdge<V>>(graph, startVertex);
		iterator.next();
		while (iterator.hasNext()) {
			V vertex = iterator.next();
			if (graph.outDegreeOf(vertex) == 0) {
				leaves.add(vertex);
			}
		}
		return leaves;
	}

	public Set<V> getLeafSuccessors(V startVertex) {
		return collectLeafVertices(this, startVertex);
	}

	public Set<V> getLeafPredecessors(V startVertex) {
		DirectedGraph<V, NamedObjectEdge<V>> graph = new EdgeReversedGraph<V, NamedObjectEdge<V>>(this);
		return collectLeafVertices(graph, startVertex);
	}

}