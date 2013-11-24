package org.javaan.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.TypeFormatter;

/**
 * Base command for all class dependency commands
 */
public abstract class BaseDependencyGraphCommand extends BaseGraphCommand<Type> {

	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	@Override
	protected String filterCriteria(CommandLine commandLine) {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	@Override
	protected ObjectFormatter<Type> getFormatter() {
		return new TypeFormatter();
	}

	@Override
	protected Collection<Type> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		Collection<Type> types = new ArrayList<Type>();
		types.addAll(classContext.getClasses());
		types.addAll(classContext.getInterfaces());
		return SortUtil.sort(FilterUtil.filter(types, new NameMatcher<Type>(filterCriteria)));
	}

	
}
