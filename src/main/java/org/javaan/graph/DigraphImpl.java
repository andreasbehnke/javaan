package org.javaan.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.javaan.SortUtil;

/**
 * Implementation of {@link Digraph} using a hash map as data structure
 */
public class DigraphImpl<N extends Comparable<? super N>> implements Digraph<N> {

	/**
	 * Stores the graphs parent child relations
	 */
	protected final Map<N, Set<N>> parentChildMap = new HashMap<N, Set<N>>();
	
	/**
	 * Stores the graphs child parent relations
	 */
	protected final Map<N, Set<N>> childParentMap = new HashMap<N, Set<N>>();
	
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
	public int size() {
		return parentChildMap.size();
	}
	
	@Override
	public Set<N> getNodes() {
		return parentChildMap.keySet();
	}
	
	@Override
	public Set<N> getChilds(N parent) {
		if (parentChildMap.containsKey(parent)) {
			return parentChildMap.get(parent);
		} else {
			return new HashSet<N>();
		}
	}
	
	@Override
	public Set<N> getParents(N child) {
		if (childParentMap.containsKey(child)) {
			return childParentMap.get(child);
		} else {
			return new HashSet<N>();
		}
	}

	private Set<N> collectLeafNodes(N node, TraversalCallback<N> callback) {
		Set<N> leaveNodes = new HashSet<N>();
		Set<N> successors = new HashSet<N>();
		Stack<N> successorStack = new Stack<N>();
		successorStack.addAll(callback.getNextForTranversal(node));
		while(!successorStack.isEmpty()) {
			N successor = successorStack.pop();
			// detect cycle, ignore existing nodes
			if (!successors.contains(successor)) {
				successors.add(successor);
				List<N> nextNodes = callback.getNextForTranversal(successor);
				if (nextNodes.size() > 0) {
					successorStack.addAll(nextNodes);
				} else {
					leaveNodes.add(successor);
				}
			}
		}
		return leaveNodes;
	}
	
	@Override
	public Set<N> getLeafParents(N child) {
		return collectLeafNodes(child, new TraversalCallback<N>() {
			@Override
			public List<N> getNextForTranversal(N node) {
				return new ArrayList<N>(getParents(node));
			}
		});
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
				successorStack.addAll(getChilds(successor));
			}
		}
		return successors;
	}
	
	@Override
	public Set<N> getPredecessors(N child) {
		Set<N> predecessors = new HashSet<N>();
		Stack<N> predecessorStack = new Stack<N>();
		predecessorStack.addAll(getParents(child));
		while(!predecessorStack.isEmpty()) {
			N predecessor = predecessorStack.pop();
			// detect cycle, ignore existing nodes
			if (!predecessors.contains(predecessor)) {
				predecessors.add(predecessor);
				predecessorStack.addAll(getParents(predecessor));
			}
		}
		return predecessors;
	}
	
	@Override
	public boolean hasChilds(N parent) {
		Set<N> childs = parentChildMap.get(parent);
		if (childs == null || childs.size() == 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean containsNode(N node) {
		return parentChildMap.containsKey(node);
	}

	@Override
	public Set<N> getLeafChilds(N parent) {
		return collectLeafNodes(parent, new TraversalCallback<N>() {
			@Override
			public List<N> getNextForTranversal(N node) {
				return new ArrayList<N>(getChilds(node));
			}
		});
	}

	private void traverseBreadthFirst(N node, int depth, Visitor<N> visitor, TraversalCallback<N> callback) {
		List<List<N>> successors = new ArrayList<List<N>>();
		List<N> firstChilds = new ArrayList<N>();
		Set<N> visited = new HashSet<N>();
		firstChilds.add(node);
		successors.add(firstChilds);
		int level = 0;
		while(!successors.isEmpty()) {
			List<N> currentChilds = successors.remove(0);
			List<N> nextChilds = null;
			for (int i=0; i<currentChilds.size(); i++) {
				N n = currentChilds.get(i);
				boolean isTail = (i == currentChilds.size() - 1);
				if (!visited.contains(n)) {
					List<N> childs = callback.getNextForTranversal(n);
					visitor.visit(n, level, childs.size() > 0, isTail);
					visited.add(n);
					if (depth < 0 || level < depth) {
						if (nextChilds == null) {
							nextChilds = new ArrayList<N>();
						}
						nextChilds.addAll(childs);
					}
				}
			}
			if (nextChilds != null) {
				successors.add(nextChilds);
			}
			level++;
		}
	}
	
	private void traverseDepthFirst(N node, int depth, Visitor<N> visitor, TraversalCallback<N> callback) {
		Stack<List<N>> stack = new Stack<List<N>>();
		List<N> firstChilds = new ArrayList<N>();
		Set<N> visited = new HashSet<N>();
		firstChilds.add(node);
		stack.push(firstChilds);
		while(!stack.empty()) {
			List<N> childList = stack.peek();
			if (childList.isEmpty()) {
				stack.pop();
			} else {
				N n = childList.remove(0);
				boolean isTail = childList.isEmpty();
				if (!visited.contains(n)) {
					int stackSize = stack.size() - 1;
					childList = callback.getNextForTranversal(n);
					visitor.visit(n, stackSize, childList.size() > 0, isTail);
					visited.add(n);
					if (depth < 0 || stackSize < depth) {
						stack.push(childList);
					}
				}
			}
		}
	}
	
	private TraversalCallback<N> getChildTraversal() {
		return new TraversalCallback<N>() {
			@Override
			public List<N> getNextForTranversal(N node) {
				return SortUtil.sort(getChilds(node));
			}
		};
	}
	
	@Override
	public void traverseSuccessorsBreadthFirst(N node, int depth, Visitor<N> visitor) {
		traverseBreadthFirst(node, depth, visitor, getChildTraversal());
	}
	
	@Override
	public void traverseSuccessorsDepthFirst(N node, int depth, Visitor<N> visitor) {
		traverseDepthFirst(node, depth, visitor, getChildTraversal());
	}
	
	private TraversalCallback<N> getParentTraversal() {
		return new TraversalCallback<N>() {
			@Override
			public List<N> getNextForTranversal(N node) {
				return SortUtil.sort(getParents(node));
			}
		};
	}

	@Override
	public void traversePredecessorsBreadthFirst(N node, int depth, Visitor<N> visitor) {
		traverseBreadthFirst(node, depth, visitor, getParentTraversal());
	}
	
	@Override
	public void traversePredecessorsDepthFirst(N node, int depth, Visitor<N> visitor) {
		traverseDepthFirst(node, depth, visitor, getParentTraversal());
	}
}
