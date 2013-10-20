package org.javaan.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleChildGraphImpl<N> implements SingleChildGraph<N> {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<N, N> parentChildMap = new HashMap<N, N>();
	
	private final Map<N, Set<N>> childParentMap = new HashMap<N, Set<N>>();
	
	@Override
	public void addNode(N node) {
		if (!parentChildMap.containsKey(node)) {
			parentChildMap.put(node, null);
		}
	}

	@Override
	public void addEdge(N parent, N child) {
		parentChildMap.put(parent, child);
		Set<N> parents = childParentMap.get(child);
		if (parents == null) {
			parents = new HashSet<N>();
			childParentMap.put(child, parents);
		}
		parents.add(parent);
		addNode(child);
	}

	@Override
	public Set<N> getNodes() {
		return parentChildMap.keySet();
	}

	@Override
	public boolean containsNode(N node) {
		return parentChildMap.containsKey(node);
	}

	@Override
	public N getChild(N parent) {
		return parentChildMap.get(parent);
	}
	
	@Override
	public Set<N> getParents(N child) {
		return childParentMap.get(child);
	}

	@Override
	public boolean hasChild(N parent) {
		return parentChildMap.get(parent) != null;
	}

	@Override
	public List<N> getPath(N node) {
		List<N> path = new ArrayList<N>();
		N firstNode = node;
		N currentNode = node;
		while(currentNode != null) {
			path.add(currentNode);
			currentNode = parentChildMap.get(currentNode);
			if (firstNode.equals(currentNode)) {
				path.add(currentNode);
				// found a cycle, stop here!
				break;
			}
		}
		return path;
	}

}
