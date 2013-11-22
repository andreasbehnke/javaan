package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;

public class ShowCallerGraph extends BaseMethodCommand {

	private final static String NAME = "callers";

	private final static String DESCRIPTION = "Display the graph of methods which call another method. "
			+ "This is the bottom up view of the call graph.";

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
		callGraph.traverseCallers(method, graphPrinter);
	}

	@Override
	protected Set<Method> collectLeafMethods(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallers(method);
	}
}
