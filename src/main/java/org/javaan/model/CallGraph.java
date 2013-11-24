package org.javaan.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javaan.graph.NamedObjectDirectedGraph;
import org.javaan.graph.NamedObjectEdge;
import org.javaan.graph.NamedObjectVisitor;
import org.jgrapht.alg.StrongConnectivityInspector;

/**
 * Represents the call-graph of all loaded methods.
 * Type dependencies are created for every method.
 */
public class CallGraph {
	
	private final NamedObjectDirectedGraph<Method> callerOfMethod = new NamedObjectDirectedGraph<Method>();

	private final NamedObjectDirectedGraph<Type> usageOfType = new NamedObjectDirectedGraph<Type>();

	public int size() {
		return callerOfMethod.vertexSet().size();
	}

	public void addCall(Method caller, Method callee) {
		if (caller == null) {
			throw new IllegalArgumentException("Parameter caller must not be null");
		}
		if (callee == null) {
			throw new IllegalArgumentException("Parameter callee must not be null");
		}
		callerOfMethod.addEdge(caller, callee);
		Type typeOfCaller = caller.getType();
		Type typeOfCallee = callee.getType();
		if (!typeOfCaller.equals(typeOfCallee)) {
			usageOfType.addEdge(typeOfCaller, typeOfCallee);
		}
	}
	
	public Set<Method> getCallers(Method callee) {
		return callerOfMethod.sourceVerticesOf(callee);
	}
	
	public Set<Method> getCallees(Method caller) {
		return callerOfMethod.targetVerticesOf(caller);
	}
	
	public void traverseCallers(Method callee, NamedObjectVisitor<Method> callerVisitor) {
		callerOfMethod.traversePredecessorsDepthFirst(callee, callerVisitor);
	}
	
	public void traverseCallees(Method caller, NamedObjectVisitor<Method> calleeVisitor) {
		callerOfMethod.traverseSuccessorsDepthFirst(caller, calleeVisitor);
	}
	
	public Set<Method> getLeafCallers(Method callee) {
		return callerOfMethod.getLeafPredecessors(callee);
	}
	
	public Set<Method> getLeafCallees(Method caller) {
		return callerOfMethod.getLeafSuccessors(caller);
	}

	public void traverseUsedTypes(Type using, NamedObjectVisitor<Type> usedVisitor) {
		usageOfType.traverseSuccessorsDepthFirst(using, usedVisitor);
	}
	
	public void traverseUsingTypes(Type used, NamedObjectVisitor<Type> usingVisitor) {
		usageOfType.traversePredecessorsDepthFirst(used, usingVisitor);
	}

	public Set<Type> getLeafUsedTypes(Type using) {
		return usageOfType.getLeafSuccessors(using);
	}
	
	public Set<Type> getLeafUsingTypes(Type using) {
		return usageOfType.getLeafPredecessors(using);
	}
	
	/**
	 * @return list of type sets which take part in a using dependency cycle
	 */
	public List<Set<Type>> getDependencyCycles() {
		StrongConnectivityInspector<Type, NamedObjectEdge<Type>> inspector = new StrongConnectivityInspector<Type, NamedObjectEdge<Type>>(usageOfType);
		List<Set<Type>> cycles = new ArrayList<Set<Type>>();
		for (Set<Type> cycle : inspector.stronglyConnectedSets()) {
			if (cycle.size() > 1) { // ignore depedency cycles within one class (these cycles have no impact in software design)
				cycles.add(cycle);
			}
		}
		return cycles;
	}
}