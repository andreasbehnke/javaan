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

abstract class BaseCallGraphCommand extends BaseCommand {

	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.METHOD);
		return options;
	}
	
	protected abstract void traverse(CallGraph callGraph, Method method, int maxDepth, GraphPrinter<Method> graphPrinter);
	
	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		String criteria = commandLine.getOptionValue(StandardOptions.OPT_METHOD);
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, types).build();
		Collection<Method> methods = SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(criteria)));
		GraphPrinter<Method> graphPrinter = new GraphPrinter<Method>(output, new MethodFormatter());
		for (Method method : methods) {
			traverse(callGraph, method, -1, graphPrinter);
			output.println();
			output.println("--");
			output.println();
		}
	}

}
