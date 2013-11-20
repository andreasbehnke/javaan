package org.javaan.model;

import java.util.Set;

import org.javaan.graph.NamedObjectDirectedGraph;
import org.javaan.graph.NamedObjectVisitor;

/**
 * Represents the call-graph of all loaded methods. Leaf nodes of the graph represent potential 
 * entry points to loaded libraries.
 */
public class CallGraph {
	
	private final NamedObjectDirectedGraph<Method> callerOfMethod = new NamedObjectDirectedGraph<Method>();
	
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
}
