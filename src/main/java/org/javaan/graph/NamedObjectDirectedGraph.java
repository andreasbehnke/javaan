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

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.javaan.model.NamedObject;
import org.jgrapht.DirectedGraph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
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

	private void traverseGraph(DirectedGraph<V, NamedObjectEdge<V>> graph, V startVertex, NamedObjectVisitor<V> visitor, boolean depthFirst) {
		if (!containsVertex(startVertex)) {
			return;
		}
		TraversalListener<V, NamedObjectEdge<V>> listener = null;
		GraphIterator<V, NamedObjectEdge<V>> iterator = null;
		if (depthFirst) {
			listener = new NamedObjectDepthFirstTraversalListener<V>(visitor);
			iterator = new DepthFirstIterator<V, NamedObjectEdge<V>>(graph, startVertex);
		} else {
			listener = new NamedObjectBreadthFirstTraversalListener<V>(visitor);
			iterator = new BreadthFirstIterator<V, NamedObjectEdge<V>>(graph, startVertex);
		}
		iterator.addTraversalListener(listener);
		while (iterator.hasNext() && !visitor.finished()) {
			iterator.next();
		}
	}

	public void traverseSuccessorsDepthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		traverseGraph(this, startVertex, visitor, true);
	}

	public void traversePredecessorsDepthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		DirectedGraph<V, NamedObjectEdge<V>> graph = new EdgeReversedGraph<V, NamedObjectEdge<V>>(this);
		traverseGraph(graph, startVertex, visitor, true);
	}

	public void traverseSuccessorsBreadthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		traverseGraph(this, startVertex, visitor, false);
	}

	public void traversePredecessorsBreadthFirst(V startVertex, NamedObjectVisitor<V> visitor) {
		DirectedGraph<V, NamedObjectEdge<V>> graph = new EdgeReversedGraph<V, NamedObjectEdge<V>>(this);
		traverseGraph(graph, startVertex, visitor, false);
	}

	private Set<V> collectLeafVertices(DirectedGraph<V, NamedObjectEdge<V>> graph, V startVertex) {
		Set<V> leaves = new HashSet<V>();
		if (graph.containsVertex(startVertex)) {
			GraphIterator<V, NamedObjectEdge<V>> iterator = new DepthFirstIterator<V, NamedObjectEdge<V>>(graph, startVertex);
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
		DirectedGraph<V, NamedObjectEdge<V>> graph = new EdgeReversedGraph<V, NamedObjectEdge<V>>(this);
		return collectLeafVertices(graph, startVertex);
	}

}