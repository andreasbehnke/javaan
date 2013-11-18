package org.javaan.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.javaan.model.NamedObject;
import org.jgrapht.graph.DefaultDirectedGraph;

public class NamedObjectDirectedGraph<V extends NamedObject> extends DefaultDirectedGraph<V, NamedObjectEdge<V>> {

	private static final long serialVersionUID = 1L;

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
}
