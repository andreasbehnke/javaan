package org.javaan.commands;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Method;
import org.javaan.print.MethodFormatter;
import org.javaan.print.ObjectFormatter;

public class ShowCallerGraph extends BaseGraphCommand<Method> {

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
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.METHOD);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	@Override
	protected String filterCriteria(CommandLine commandLine) {
		return commandLine.getOptionValue(StandardOptions.OPT_METHOD);
	}

	@Override
	protected Collection<Method> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(filterCriteria)));
	}
	
	@Override
	protected ObjectFormatter<Method> getFormatter() {
		return new MethodFormatter();
	}

	@Override
	protected boolean isPrintLeaves(CommandLine commandLine) {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}

	@Override
	protected void traverse(CallGraph callGraph, Method method, NamedObjectVisitor<Method> graphPrinter) {
		callGraph.traverseCallers(method, graphPrinter);
	}

	@Override
	protected Set<Method> collectLeafObjects(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallers(method);
	}
}
