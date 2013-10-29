package org.javaan.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SingleChildGraphImpl<N extends Comparable<? super N>> extends DigraphImpl<N> implements SingleChildGraph<N> {

	@Override
	public void addEdge(N parent, N child) {
		if (hasChilds(parent)) {
			throw new IllegalArgumentException("Parent node " + parent + " already contains a child!");
		}
		super.addEdge(parent, child);
	}
	
	@Override
	public N getChild(N parent) {
		Set<N> childs = parentChildMap.get(parent);
		if (childs == null || childs.size() == 0) {
			return null;
		}
		return childs.iterator().next();
	}

	@Override
	public List<N> getPath(N node) {
		List<N> path = new ArrayList<N>();
		N firstNode = node;
		N currentNode = node;
		while(currentNode != null) {
			path.add(currentNode);
			currentNode = getChild(currentNode);
			if (firstNode.equals(currentNode)) {
				path.add(currentNode);
				// found a cycle, stop here!
				break;
			}
		}
		return path;
	}

}
