package org.javaan.graph;

import org.javaan.model.NamedObject;
import org.javaan.model.NamedObjectBase;

/**
 * Edge for {@link NamedObject} instances as vertices.
 */
public class NamedObjectEdge extends NamedObjectBase {

	private final NamedObject source;
	
	private final NamedObject target;

	public NamedObjectEdge(NamedObject source, NamedObject target) {
		super(buildName(source, target));
		this.source = source;
		this.target = target;
	}
	
	private static String buildName(NamedObject source, NamedObject target) {
		return source.getName() + " -> " + target.getName();
	}

	public NamedObject getSource() {
		return source;
	}

	public NamedObject getTarget() {
		return target;
	}
}
