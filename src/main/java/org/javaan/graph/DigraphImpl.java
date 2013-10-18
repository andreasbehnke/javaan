package org.javaan.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of {@link Digraph} using a hash map as data structure
 */
public class DigraphImpl<N> implements Digraph<N> {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<N, Set<N>> nodeMap = new HashMap<N, Set<N>>();

	@Override
	public void addNode(N node) {
		if (!nodeMap.containsKey(node)) {
			nodeMap.put(node, new HashSet<N>());
		}
	}
	
	@Override
	public void addEdge(N parent, N child) {
		if (containsNode(parent)) {
			nodeMap.get(parent).add(child);
		} else {
			Set<N> childs = new HashSet<N>();
			childs.add(child);
			nodeMap.put(parent, childs);
		}
		addNode(child);
	}
	
	@Override
	public Set<N> getNodes() {
		return nodeMap.keySet();
	}
	
	@Override
	public Set<N> getChilds(N parent) {
		return nodeMap.get(parent);
	}
	
	@Override
	public Set<N> getSuccessors(N parent) {
		Set<N> successors = new HashSet<N>();
		Stack<N> successorStack = new Stack<N>();
		successorStack.addAll(getChilds(parent));
		while(!successorStack.isEmpty()) {
			N successor = successorStack.pop();
			// detect cycle, ignore existing nodes
			if (!successors.contains(successor)) {
				successors.add(successor);
				Set<N> successorOfSuccessor = getChilds(successor);
				if (successorOfSuccessor.size() > 0) {
					// more callers to detect
					successorStack.addAll(successorOfSuccessor);
				}
			}
		}
		return successors;
	}
	
	@Override
	public boolean hasChilds(N parent) {
		return nodeMap.get(parent).size() > 0;
	}
	
	@Override
	public boolean containsNode(N node) {
		return nodeMap.containsKey(node);
	}
	
	@Override
	public Set<N> getLeaveNodes(N node) {
		Set<N> leaveNodes = new HashSet<N>();
		Stack<N> successors = new Stack<N>();
		successors.addAll(getChilds(node));
		while(!successors.isEmpty()) {
			N successor = successors.pop();
			// detect cycle, ignore self
			if (!successor.equals(node)) {
				Set<N> successorOfSuccessor = getChilds(successor);
				if (successorOfSuccessor.size() > 0) {
					// more callers to detect
					successors.addAll(successorOfSuccessor);
				} else {
					// leave node found
					leaveNodes.add(successor);
				}
			}
		}
		return leaveNodes;
	}
}
