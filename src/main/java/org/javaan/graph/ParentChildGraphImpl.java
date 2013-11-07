package org.javaan.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParentChildGraphImpl<P, C> implements ParentChildGraph<P, C> {

	private final Map<P, Set<C>> parentChildMap = new HashMap<P, Set<C>>();
	
	private final Map<C, Set<P>> childParentMap = new HashMap<C, Set<P>>();
	
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
		Set<P> parents = childParentMap.get(child);
		Set<C> childs = parentChildMap.get(parent);
		if (childs == null) {
			childs = new HashSet<C>();
			parentChildMap.put(parent, childs);
		}
		if (parents == null) {
			parents = new HashSet<P>();
			childParentMap.put(child, parents);
		}
		childs.add(child);
		parents.add(parent);
	}

	@Override
	public boolean containsChild(C child) {
		return childParentMap.containsKey(child);
	}
	
	@Override
	public Set<C> getChilds() {
		return childParentMap.keySet();
	}
	
	@Override
	public Set<C> getChilds(P parent) {
		Set<C> childs = parentChildMap.get(parent);
		if (childs == null) {
			return new HashSet<C>();
		}
		return childs;
	}

	@Override
	public Set<P> getParents(C child) {
		Set<P> parents = childParentMap.get(child);
		if (parents == null) {
			return new HashSet<P>();
		}
		return parents;
	}

}
