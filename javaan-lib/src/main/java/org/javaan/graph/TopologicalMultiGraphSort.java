package org.javaan.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

/**
 * Provides a topological sort for a {@link DirectedMultigraph} (may not contain loops
 * but multiple edges between one pair of vertices). Order starts with vertex having
 * in-degree = 0 and lowest out-degree. A tie is broken by sorting vertices by the number of
 * edges to the current vertex.
 */
public class TopologicalMultiGraphSort<V, E> {

	private static class ModifiableInteger {

		private int value;

		public ModifiableInteger(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void subtract(int sub) {
			value -= sub;
		}
	}

	private final Graph<V, E> graph;

	private Queue<V> queue;

	private Queue<V> startVertexQueue;

	private Map<V, ModifiableInteger> inDegreeMap;

	public TopologicalMultiGraphSort(Graph<V, E> graph) {
		this.graph = graph;
	}

	/**
	 * Creates a indegree map and the queue to be processed
	 */
	private void initializeDatastructures() {
		LinkedList<V> vertexList = new LinkedList<>();
		inDegreeMap = new HashMap<>();
		// collect all start vertices and build the in-degree map
		for (V v : graph.vertexSet()) {
			int inDegree = graph.inDegreeOf(v);
			if (inDegree == 0) {
				vertexList.offer(v);
			}
			inDegreeMap.put(v, new ModifiableInteger(inDegree));
		}
		// sort queue by out-degree of vertex in ascending order. Vertices with lower
		// out-degree should be processed first
		vertexList.sort((o1, o2) -> {
			int outDegree1 = graph.outDegreeOf(o1);
			int outDegree2 = graph.outDegreeOf(o2);
			return outDegree1 - outDegree2;
		});
		startVertexQueue = vertexList;
		queue = new LinkedList<>();
		queue.offer(startVertexQueue.remove());// add first start vertex to queue
	}

	private void addVertexIfNotContained(List<V> list, V vertex) {
		if (!list.contains(vertex)) {
			list.add(vertex);
		}
	}

	private List<V> getTargetVerticesSortedByDegree(final V vertex) {
		List<V> vertices = new ArrayList<>(DirectedGraphUtils.targetVerticesOf(vertex, graph));
		// sort target vertices to be processed by	2 number of edges
		// between target vertex and source vertex
		vertices.sort((o1, o2) -> {
			int degree1 = graph.getAllEdges(vertex, o1).size();
			int degree2 = graph.getAllEdges(vertex, o2).size();
			return degree2 - degree1;
		});
		return vertices;
	}

	public List<V> sort() {
		initializeDatastructures();
		List<V> sorted = new ArrayList<>();

		while(!queue.isEmpty()) {
			V vertex = queue.remove();
			addVertexIfNotContained(sorted, vertex);
			List<V> targets = getTargetVerticesSortedByDegree(vertex);
			for (V target : targets) {
				// subtract number of edges from in-degree map
				int numberOfEdges = graph.getAllEdges(vertex, target).size();
				ModifiableInteger modInt = inDegreeMap.get(target);
				modInt.subtract(numberOfEdges);
				if (modInt.getValue() == 0) {
					queue.offer(target);
				}
			}
			if (queue.isEmpty() && !startVertexQueue.isEmpty()) {
				queue.offer(startVertexQueue.remove());
			}
		}
		return sorted;
	}
}
