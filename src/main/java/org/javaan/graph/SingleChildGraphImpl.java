package org.javaan.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleChildGraphImpl<N> implements SingleChildGraph<N> {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<N, N> nodeMap = new HashMap<N, N>();
	
	@Override
	public void addNode(N node) {
		if (!nodeMap.containsKey(node)) {
			nodeMap.put(node, null);
		}
	}

	@Override
	public void addEdge(N parent, N child) {
		nodeMap.put(parent, child);
		addNode(child);
	}

	@Override
	public Set<N> getNodes() {
		return nodeMap.keySet();
	}

	@Override
	public boolean containsNode(N node) {
		return nodeMap.containsKey(node);
	}

	@Override
	public N getChild(N parent) {
		return nodeMap.get(parent);
	}

	@Override
	public boolean hasChild(N parent) {
		return nodeMap.get(parent) != null;
	}

	@Override
	public List<N> getPath(N node) {
		List<N> path = new ArrayList<N>();
		N firstNode = node;
		N currentNode = node;
		while(currentNode != null) {
			path.add(currentNode);
			currentNode = nodeMap.get(currentNode);
			if (firstNode.equals(currentNode)) {
				path.add(currentNode);
				// found a cycle, stop here!
				break;
			}
		}
		return path;
	}

}
