package org.javaan.model;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.DirectedGraph;

/**
 * Represents a dependency between two types
 */
public class Dependency extends NamedObjectBase {
	
	private final Set<Method> callees;
	
	private final NamedObject source;
	
	private final NamedObject target;
	
	private Dependency(NamedObject source, NamedObject target, Method callee) {
		super(createName(source, target));
		this.source = source;
		this.target = target;
		callees = new HashSet<>();
		callees.add(callee);
	}

	private static String createName(NamedObject source, NamedObject target) {
		return source.getName() + "-->" + target.getName();
	}
	
	private void addCallee(Method callee) {
		callees.add(callee);
	}
	
	public Set<Method> getCallees() {
		return callees;
	}
	
	public NamedObject getSource() {
		return source;
	}
	
	public NamedObject getTarget() {
		return target;
	}
	
	public static <V extends NamedObject> void addDependency(DirectedGraph<V, Dependency> graph, V source, V target, Method callee) {
		Dependency dependency = graph.getEdge(source, target);
		if (dependency == null) {
			graph.addEdge(source, target, new Dependency(source, target, callee));
		} else {
			dependency.addCallee(callee);
		}
	}
}