package org.javaan.graph;

/**
 * Represents a potential cut point for a graph cycle
 */
public class CutPoint<V, E> {
	private final V source;
	
	private final V target;

	public CutPoint(V source, V target) {
		this.source = source;
		this.target = target;
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CutPoint<?, ?> other = (CutPoint<?, ?>) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CutPoint [source=" + source + ", target=" + target + "]";
	}
}