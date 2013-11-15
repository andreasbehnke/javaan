package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.Visitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;

public class ShowCalleeGraph extends BaseCallGraphCommand {

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
	protected void traverse(CallGraph callGraph, Method method, int maxDepth, Visitor<Method> graphPrinter) {
		callGraph.traverseCallees(method, -1, graphPrinter);
	}
	
	@Override
	protected Set<Method> collectLeafMethods(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallees(method);
	}

}
