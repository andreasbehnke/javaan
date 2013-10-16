package org.javaan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SingleChildGraph<N> extends SimpleGraph<N> {
	
	@Override
	public void addEdge(N parent, N child) {
		Set<N> childs = new HashSet<N>();
		childs.add(child);
		nodeMap.put(parent, childs);
	}

}
