package org.javaan.model;

import org.jgrapht.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a dependency between two types
 */
public class Dependency extends NamedObjectBase {

	private final Set<Method> callee;

	private final NamedObject source;

	private final NamedObject target;

	private Dependency(NamedObject source, NamedObject target, Method callee) {
		super(createName(source, target));
		this.source = source;
		this.target = target;
		this.callee = new HashSet<>();
		this.callee.add(callee);
	}

	private static String createName(NamedObject source, NamedObject target) {
		return source.getName() + "-->" + target.getName();
	}

	private void addCallee(Method callee) {
		this.callee.add(callee);
	}

	public Set<Method> getCallee() {
		return callee;
	}

	NamedObject getSource() {
		return source;
	}

	NamedObject getTarget() {
		return target;
	}

	static <V extends NamedObject> void addDependency(Graph<V, Dependency> graph, V source, V target, Method callee) {
		Dependency dependency = graph.getEdge(source, target);
		if (dependency == null) {
			graph.addEdge(source, target, new Dependency(source, target, callee));
		} else {
			dependency.addCallee(callee);
		}
	}
}
