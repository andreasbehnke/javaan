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
	private final Map<N, Set<N>> parentChildMap = new HashMap<N, Set<N>>();
	
	/**
	 * Stores the graphs child parent relations
	 */
	private final Map<N, Set<N>> childParentMap = new HashMap<N, Set<N>>();
	
	@Override
	public void addNode(N node) {
		if (!parentChildMap.containsKey(node)) {
			parentChildMap.put(node, new HashSet<N>());
		}
	}
	
	@Override
	public void addEdge(N parent, N child) {
		Set<N> childs = parentChildMap.get(parent);
		if (childs == null) {
			childs = new HashSet<N>();
			parentChildMap.put(parent, childs);
		}
		childs.add(child);
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
	public Set<N> getChilds(N parent) {
		return parentChildMap.get(parent);
	}
	
	@Override
	public Set<N> getParents(N child) {
		return childParentMap.get(child);
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
		return parentChildMap.get(parent).size() > 0;
	}
	
	@Override
	public boolean containsNode(N node) {
		return parentChildMap.containsKey(node);
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
