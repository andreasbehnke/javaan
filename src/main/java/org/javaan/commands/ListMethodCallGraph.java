package org.javaan.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;
import org.javaan.CallGraphBuilder;
import org.javaan.ClassContextBuilder;
import org.javaan.FilterUtil;
import org.javaan.MethodMatcher;
import org.javaan.SortUtil;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Method;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.MethodFormatter;

public class ListMethodCallGraph extends BaseCommand {

	private final static String NAME = "listMethodCallGraph";

	private final static String DESCRIPTION = "Display the graph of methods being called by methods defined with option --filter '<class> - <method signature>'.";

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
		StandardOptions.FILTER.setRequired(true);
		options.addOption(StandardOptions.FILTER);
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		String criteria = commandLine.getOptionValue(StandardOptions.OPT_FILTER);
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, types).build();
		Collection<Method> methods = SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(criteria)));
		GraphPrinter<Method> graphPrinter = new GraphPrinter<Method>(output, new MethodFormatter());
		for (Method method : methods) {
			callGraph.traverseCallees(method, -1, graphPrinter);
			output.println();
			output.println("--");
			output.println();
		}
	}

}
