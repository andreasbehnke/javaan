package org.javaan.graph;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TiernanSimpleCycles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given a directed <a href="http://mathworld.wolfram.com/Pseudograph.html">pseudograph</a> g, 
 * this algorithm produces a directed acyclic graph g' by breaking cycles between vertices with 
 * minimum number of edges between them
 */
public class MinimumEdgesCycleCut<V, E> {

	private final Graph<V, E> sourceGraph;
	
	private final Graph<V, E> targetGraph;

	public MinimumEdgesCycleCut(Graph<V, E> source, Graph<V, E> target) {
		this.sourceGraph = source;
		this.targetGraph = target;
	}
	
	private DirectedSimpleCycles<V, E> createCycleDetector() {
		// Tiernan's algorithm is the only implementation of DirectedSimpleCycles,
		// which can handle DirectedPseudographs. All other implementations return duplicate
		// cycles if two vertices have more than one edge connecting them.
		return new TiernanSimpleCycles<>(sourceGraph);
	}
	
	private CutPoint<V, E> findCutPoint(List<V> cycle) {
		V cutPointSource = null;
		V cutPointTarget = null;
		int minEdgeCount = Integer.MAX_VALUE;
		V source = cycle.get(cycle.size() - 1);
		for (V target : cycle) {
			int edgeCount = sourceGraph.getAllEdges(source, target).size();
			if (edgeCount < minEdgeCount) {
				cutPointSource = source;
				cutPointTarget = target;
				minEdgeCount = edgeCount;
			}
			source = target;
		}
		return new CutPoint<>(cutPointSource, cutPointTarget);
	}

	public List<CutPoint<V, E>> findCutPoints() {
		List<List<V>> cycles = createCycleDetector().findSimpleCycles();
		List<CutPoint<V, E>> cutPoints = new ArrayList<>();
		for (List<V> cycle : cycles) {
			cutPoints.add(findCutPoint(cycle));
		}
		return cutPoints;
	}
	
	public Graph<V, E> cutCycles() {
		Set<CutPoint<V, E>> cutPoints = new HashSet<>(findCutPoints());
		Graphs.addAllVertices(targetGraph, sourceGraph.vertexSet());
		for (E e : sourceGraph.edgeSet()) {
            V s = sourceGraph.getEdgeSource(e);
            V t = sourceGraph.getEdgeTarget(e);
            CutPoint<V, E> cp = new CutPoint<>(s, t);
            if (!cutPoints.contains(cp)) {
            	targetGraph.addEdge(s, t, e);
            }
        }
		return targetGraph;
	}
}