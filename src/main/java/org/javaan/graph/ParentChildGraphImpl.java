package org.javaan.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParentChildGraphImpl<P, C> implements ParentChildGraph<P, C> {

	private final Map<P, Set<C>> parentChildMap = new HashMap<P, Set<C>>();
	
	private final Map<C, P> childParentMap = new HashMap<C, P>();
	
	@Override
	public void addParent(P parent) {
		if (!parentChildMap.containsKey(parent)) {
			parentChildMap.put(parent, new HashSet<C>());
		}
	}
	
	@Override
	public boolean containsParent(P parent) {
		return parentChildMap.containsKey(parent);
	}

	@Override
	public void addEdge(P parent, C child) {
		childParentMap.put(child, parent);
		Set<C> childs = parentChildMap.get(parent);
		if (childs == null) {
			childs = new HashSet<C>();
			parentChildMap.put(parent, childs);
		}
		childs.add(child);
	}

	@Override
	public boolean containsChild(C child) {
		return childParentMap.containsKey(child);
	}
	
	@Override
	public Set<C> getChilds(P parent) {
		return parentChildMap.get(parent);
	}

	@Override
	public P getParent(C child) {
		return childParentMap.get(child);
	}

}
