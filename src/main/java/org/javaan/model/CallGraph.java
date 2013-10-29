package org.javaan.model;

import java.util.Set;

import org.javaan.graph.Digraph;
import org.javaan.graph.DigraphImpl;

/**
 * Represents the call-graph of all loaded JavaClasses. Leave nodes of the graph represent potential 
 * entry points to loaded libraries.
 */
public class CallGraph {
	
	private final Digraph<Method> callerOfMethod = new DigraphImpl<Method>();

	public void addCall(Method caller, Method callee) {
		callerOfMethod.addEdge(caller, callee);
	}
	
	public Set<Method> getCallers(Method callee) {
		return callerOfMethod.getParents(callee);
	}
	
	public Set<Method> getCallees(Method caller) {
		return callerOfMethod.getChilds(caller);
	}
	
	public void traverseCallers(Method callee, int depth, MethodVisitor callerVisitor) {
		callerOfMethod.traversePredecessorsDepthFirst(callee, depth, callerVisitor);
	}
	
	public void traverseCallees(Method caller, int depth, MethodVisitor calleeVisitor) {
		callerOfMethod.traverseSuccessorsDepthFirst(caller, depth, calleeVisitor);
	}
}
