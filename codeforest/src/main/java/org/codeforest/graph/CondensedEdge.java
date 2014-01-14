package org.codeforest.graph;

import java.util.HashSet;
import java.util.Set;

public class CondensedEdge<V, E> {

	private final V target;
	
	private final V source;
	
	private final Set<E> fromTargetToSource = new HashSet<>();
	
	private final Set<E> fromSourceToTarget = new HashSet<>();

	public CondensedEdge(V target, V source) {
		this.target = target;
		this.source = source;
	}

	public V getTarget() {
		return target;
	}

	public V getSource() {
		return source;
	}

	public Set<E> getEdgesFromTargetToSource() {
		return fromTargetToSource;
	}

	public Set<E> getEdgesFromSourceToTarget() {
		return fromSourceToTarget;
	}
	
	public void addEdge(V source, V target, E edge) {
		if (this.source == source) {
			this.fromSourceToTarget.add(edge);
		} else if (this.source == target) {
			this.fromTargetToSource.add(edge);
		}
	}
}
