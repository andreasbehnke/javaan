package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;

public class ShowCalleeGraph extends BaseMethodCommand {

	private final static String NAME = "callees";

	private final static String DESCRIPTION = "Display the graph of methods being called by another method. "
			+ "This is the top down view of the call graph.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Method method, NamedObjectVisitor<Method> graphPrinter) {
		callGraph.traverseCallees(method, graphPrinter);
	}
	
	@Override
	protected Set<Method> collectLeafMethods(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallees(method);
	}

}
