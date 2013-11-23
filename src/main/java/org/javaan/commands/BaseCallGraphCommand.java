package org.javaan.commands;

import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Method;
import org.javaan.print.MethodFormatter;
import org.javaan.print.ObjectFormatter;

/**
 * Base command for all method call graph commands
 */
public abstract class BaseCallGraphCommand extends BaseGraphCommand<Method> {

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
	protected ObjectFormatter<Method> getFormatter() {
		return new MethodFormatter();
	}

	@Override
	protected Collection<Method> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(filterCriteria)));
	}

}
