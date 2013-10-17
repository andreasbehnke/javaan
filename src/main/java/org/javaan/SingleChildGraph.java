package org.javaan;

import java.util.HashSet;
import java.util.Set;

public class SingleChildGraph<N> extends SimpleGraph<N> {
	
	@Override
	public void addEdge(N parent, N child) {
		Set<N> childs = new HashSet<N>();
		childs.add(child);
		nodeMap.put(parent, childs);
		if (!containsNode(child)) {
			addNode(child);
		}
	}

}
