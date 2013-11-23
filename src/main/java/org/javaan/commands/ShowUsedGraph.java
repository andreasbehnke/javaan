package org.javaan.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.TypeFormatter;

public class ShowUsedGraph extends BaseGraphCommand<Type> {

	private final static String NAME = "used";

	private final static String DESCRIPTION = "Display the graph of classes being used by another class. "
			+ "This is the top down view of the class dependency graph.";

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
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	@Override
	protected boolean isPrintLeaves(CommandLine commandLine) {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
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
	
	@Override
	protected Set<Type> collectLeafObjects(CallGraph callGraph, Type namedObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void traverse(CallGraph callGraph, Type namedObject, NamedObjectVisitor<Type> graphPrinter) {
		callGraph.traverseUsedTypes(namedObject, graphPrinter);
	}
}
