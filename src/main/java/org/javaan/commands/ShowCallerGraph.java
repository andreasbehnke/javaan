package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.Visitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;

public class ShowCallerGraph extends BaseCallGraphCommand {

	private final static String NAME = "callers";

	private final static String DESCRIPTION = "Display the graph of methods which call methods defined with option method.";

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
		callGraph.traverseCallers(method, -1, graphPrinter);
	}

	@Override
	protected Set<Method> collectLeafMethods(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallers(method);
	}
}
