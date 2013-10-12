package org.javaan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a digraph without multi-edges.  
 * Provides methods for querying graph.
 */
public class Graph {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<String, Set<String>> nodeMap = new HashMap<String, Set<String>>();

	public void addNode(String node) {
		nodeMap.put(node, new HashSet<String>());
	}
	
	public void addEdge(String parent, String child) {
		if (containsNode(parent)) {
			nodeMap.get(parent).add(child);
		} else {
			Set<String> childs = new HashSet<String>();
			childs.add(child);
			nodeMap.put(parent, childs);
		}
		if (!containsNode(child)) {
			addNode(child);
		}
	}
	
	public Set<String> getChilds(String parent) {
		return nodeMap.get(parent);
	}
	
	public boolean hasChilds(String parent) {
		return nodeMap.get(parent).size() > 0;
	}
	
	public boolean containsNode(String node) {
		return nodeMap.containsKey(node);
	}
	
	public Set<String> getLeaveNodes(String node) {
		Set<String> leaveNodes = new HashSet<String>();
		Stack<String> ancestors = new Stack<String>();
		ancestors.addAll(getChilds(node));
		while(!ancestors.isEmpty()) {
			String ancestor = ancestors.pop();
			// detect cycle, ignore self
			if (!ancestor.equals(node)) {
				Set<String> ancestorsOfAncestor = getChilds(ancestor);
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
