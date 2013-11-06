package org.javaan.graph;

import java.util.Set;

/**
 * Represent a digraph with depth of 1 and different node types for parent and child
 * 
 * @param <P> The parent type
 * @param <C> The child type
 */
public interface ParentChildGraph<P, C> {

	void addParent(P parent);
	
	boolean containsParent(P parent);
	
	void addEdge(P parent, C child);
	
	boolean containsChild(C child);
	
	Set<C> getChilds();
	
	Set<C> getChilds(P parent);
	
	Set<P> getParents(C child);
}
