package org.javaan.commands;

import org.javaan.model.CallGraph;
import org.javaan.model.Method;
import org.javaan.print.GraphPrinter;

public class ShowCalleeGraph extends BaseCallGraphCommand {

	private final static String NAME = "callee";

	private final static String DESCRIPTION = "Display the graph of methods being called by methods defined with option method.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Method method, int maxDepth, GraphPrinter<Method> graphPrinter) {
		callGraph.traverseCallees(method, -1, graphPrinter);
	}

}
