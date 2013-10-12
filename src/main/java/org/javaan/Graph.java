package org.javaan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a digraph without multi-edges.  
 * Provides methods for querying graph.
 * 
 * @param <N> type of graph nodes
 */
public class Graph<N> {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<N, Set<N>> nodeMap = new HashMap<N, Set<N>>();

	public void addNode(N node) {
		nodeMap.put(node, new HashSet<N>());
	}
	
	public void addEdge(N parent, N child) {
		if (containsNode(parent)) {
			nodeMap.get(parent).add(child);
		} else {
			Set<N> childs = new HashSet<N>();
			childs.add(child);
			nodeMap.put(parent, childs);
		}
		if (!containsNode(child)) {
			addNode(child);
		}
	}
	
	public Set<N> getChilds(N parent) {
		return nodeMap.get(parent);
	}
	
	public boolean hasChilds(N parent) {
		return nodeMap.get(parent).size() > 0;
	}
	
	public boolean containsNode(N node) {
		return nodeMap.containsKey(node);
	}
	
	public Set<N> getLeaveNodes(N node) {
		Set<N> leaveNodes = new HashSet<N>();
		Stack<N> ancestors = new Stack<N>();
		ancestors.addAll(getChilds(node));
		while(!ancestors.isEmpty()) {
			N ancestor = ancestors.pop();
			// detect cycle, ignore self
			if (!ancestor.equals(node)) {
				Set<N> ancestorsOfAncestor = getChilds(ancestor);
				if (ancestorsOfAncestor.size() > 0) {
					// more callers to detect
					ancestors.addAll(ancestorsOfAncestor);
				} else {
					// entry method found
					leaveNodes.add(ancestor);
				}
			}
		}
		return leaveNodes;
	}
}
