package org.javaan.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;

/**
 * Provides a topological sort of all strongly connected subgraphs of a 
 * non acyclic directed graph (may contain loops and multiple edges between 
 * one pair of vertices). Order starts with vertices of the subgraph having 
 * lowest out-degree. A tie is broken by sorting vertices by the number of 
 * edges to the current vertex.
 * 
 * This class is not thread safe.
 */
public class TopologicalMultigraphSort<V, E> {

	private static class ModifiableInteger {
		
		private int value;

		public ModifiableInteger(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	
		public void substract(int sub) {
			value -= sub;
		}
	}
	
	private final DirectedGraph<V, E> graph;
	
	private Queue<V> queue;

	private Map<V, ModifiableInteger> inDegreeMap;
	
	public TopologicalMultigraphSort(DirectedGraph<V, E> graph) {
		this.graph = graph;
	}

	/**
	 * Creates a indegree map and the queue to be processed
	 */
	private void initializeDatastructures(DirectedGraph<V, E> subgraph) {
		LinkedList<V> vertexList = new LinkedList<>();
		inDegreeMap = new HashMap<V, TopologicalMultigraphSort.ModifiableInteger>();
		// collect all start vertices and build the in-degree map
		for (V v : subgraph.vertexSet()) {
			int inDegree = graph.inDegreeOf(v);
			if (inDegree == 0) {
				vertexList.offer(v);
			}
			inDegreeMap.put(v, new ModifiableInteger(inDegree));
		}
		// sort queue by out-degree of vertex in ascending order. Vertices with less 
		// out-degree should be processed first
		Collections.sort(vertexList, new Comparator<V>() {
			@Override
			public int compare(V o1, V o2) {
				int outDegree1 = graph.outDegreeOf(o1);
				int outDegree2 = graph.outDegreeOf(o2);
				return outDegree1 - outDegree2;
			}
		});
		this.queue = vertexList;
	}
	
	private void addVertexIfnotContained(List<V> list, V vertex) {
		if (!list.contains(vertex)) {
			list.add(vertex);
		}
	}

	private List<V> getTargetVerticesSortedByDegree(final V vertex, final DirectedGraph<V, E> subgraph) {
		List<V> vertices = new ArrayList<>(DirectedGraphUtils.targetVerticesOf(vertex, subgraph));
		// sort target vertices to be processed by number of edges 
		// between target vertex and source vertex
		Collections.sort(vertices, new Comparator<V>() {

			@Override
			public int compare(V o1, V o2) {
				int degree1 = subgraph.getAllEdges(vertex, o1).size();
				int degree2 = subgraph.getAllEdges(vertex, o2).size();
				return degree2 - degree1;
			}
		});
		return vertices;
	}
	
	public List<V> sort() {
		List<V> sorted = new ArrayList<>();
		List<DirectedSubgraph<V, E>> stronglyConnectedSubgraphs = new StrongConnectivityInspector<V, E>(graph).stronglyConnectedSubgraphs();
		// process all strongly connected subgraphs (circles)
		for (DirectedGraph<V, E> subgraph : stronglyConnectedSubgraphs) {
			// for each subgraph, add all vertices which are not 
			// already contained in result in topological order.
			initializeDatastructures(subgraph);
			while(!queue.isEmpty()) {
				V vertex = queue.remove();
				addVertexIfnotContained(sorted, vertex);
				List<V> targets = getTargetVerticesSortedByDegree(vertex, subgraph);
				for (V target : targets) {
					// subtract number of edges from in-degree map
					int numberOfEdges = subgraph.getAllEdges(vertex, target).size();
					ModifiableInteger modInt = inDegreeMap.get(target);
					modInt.substract(numberOfEdges);
					if (modInt.getValue() == 0) {
						queue.offer(target);
					}
				}
			}
		}
		return sorted;
	}
	
}