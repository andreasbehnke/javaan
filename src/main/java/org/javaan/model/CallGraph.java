package org.javaan.model;

import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.DigraphImpl;
import org.javaan.graph.Visitor;

/**
 * Represents the call-graph of all loaded JavaClasses. Leave nodes of the graph represent potential 
 * entry points to loaded libraries.
 */
public class CallGraph {
	
	private final Digraph<Method> callerOfMethod = new DigraphImpl<Method>();
	
	public int size() {
		return callerOfMethod.size();
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
		return callerOfMethod.getParents(callee);
	}
	
	public Set<Method> getCallees(Method caller) {
		return callerOfMethod.getChilds(caller);
	}
	
	public void traverseCallers(Method callee, int depth, Visitor<Method> callerVisitor) {
		callerOfMethod.traversePredecessorsDepthFirst(callee, depth, callerVisitor);
	}
	
	public void traverseCallees(Method caller, int depth, Visitor<Method> calleeVisitor) {
		callerOfMethod.traverseSuccessorsDepthFirst(caller, depth, calleeVisitor);
	}
}
