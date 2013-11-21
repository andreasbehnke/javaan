package org.javaan.graph;

import java.util.HashSet;
import java.util.Set;

import org.javaan.model.NamedObject;
import org.javaan.model.NamedObjectBase;

/**
 * Edge for {@link NamedObject} instances as vertices.
 */
public class NamedObjectEdge<V extends NamedObject> extends NamedObjectBase {

	private final V source;
	
	private final V target;

	public NamedObjectEdge(V source, V target) {
		super(buildName(source, target));
		this.source = source;
		this.target = target;
	}
	
	private static String buildName(NamedObject source, NamedObject target) {
		return source.getName() + " -> " + target.getName();
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}
	
	public static <V extends NamedObject> Set<V> getTargetSet(Set<NamedObjectEdge<V>> edges) {
		Set<V> namedObjects = new HashSet<V>();
		for (NamedObjectEdge<V> namedObjectEdge : edges) {
			namedObjects.add(namedObjectEdge.getTarget());
		}
		return namedObjects;
	}
	
	public static <V extends NamedObject> Set<V> getSourceSet(Set<NamedObjectEdge<V>> edges) {
		Set<V> namedObjects = new HashSet<V>();
		for (NamedObjectEdge<V> namedObjectEdge : edges) {
			namedObjects.add(namedObjectEdge.getSource());
		}
		return namedObjects;
	}
}
