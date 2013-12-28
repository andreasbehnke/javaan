package org.javaan.graph;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BidirectionalMap<P, C> {

	private final Map<P, Set<C>> parentChildMap = new HashMap<P, Set<C>>();
	
	private final Map<C, Set<P>> childParentMap = new HashMap<C, Set<P>>();

	public void addParent(P parent) {
		if (!parentChildMap.containsKey(parent)) {
			parentChildMap.put(parent, new HashSet<C>());
		}
	}

	public boolean containsParent(P parent) {
		return parentChildMap.containsKey(parent);
	}

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

	public boolean containsChild(C child) {
		return childParentMap.containsKey(child);
	}

	public Set<C> getChilds() {
		return childParentMap.keySet();
	}

	public Set<C> getChilds(P parent) {
		Set<C> childs = parentChildMap.get(parent);
		if (childs == null) {
			return new HashSet<C>();
		}
		return childs;
	}

	public Set<P> getParents(C child) {
		Set<P> parents = childParentMap.get(child);
		if (parents == null) {
			return new HashSet<P>();
		}
		return parents;
	}

}
