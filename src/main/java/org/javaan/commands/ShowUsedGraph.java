package org.javaan.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.TypeFormatter;

public class ShowUsedGraph extends BaseTypeLoadingCommand {

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
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, types).build();
		Collection<Clazz> classes = classContext.getClasses();
		if (commandLine.hasOption(StandardOptions.OPT_FILTER)) {
			String criteria = commandLine.getOptionValue(StandardOptions.OPT_FILTER);
			classes = FilterUtil.filter(classes, new NameMatcher<Clazz>(criteria)); 
		}
		classes = SortUtil.sort(classes);
		ObjectFormatter<Type> formatter = new TypeFormatter();
		NamedObjectVisitor<Type> visitor = new GraphPrinter<Type>(output, formatter);
		for (Clazz clazz : classes) {
			output.println(String.format("%s:",formatter.format(clazz)));
			callGraph.traverseUsedTypes(clazz, visitor);
			output.println("\n--\n");
		}
	}

}
