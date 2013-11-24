package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;
import org.javaan.print.TypeFormatter;

public class ListDepdendencyCycles extends BaseTypeLoadingCommand {

	private final static String NAME = "dependencyCycles";
	
	private final static String DESCRIPTION = "Lists all related classes for each dependency cycle in the loaded libraries.";
	
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
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, types).build();
		List<Set<Type>> cycles = callGraph.getDependencyCycles();
		printCycles(output, cycles);
	}
	
	public void printCycles(PrintStream output, List<Set<Type>> cycles) {
		ObjectFormatter<Type> formatter = new TypeFormatter();
		int index = 1;
		for (Set<Type> cycle : cycles) {
			PrintUtil.println(output, formatter, SortUtil.sort(cycle), "Cycle " + index + ": ", "\t\n", ", ");
			output.println(PrintUtil.BLOCK_SEPARATOR);
			index++;
		}
	}
}
