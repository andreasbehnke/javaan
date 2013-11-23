package org.javaan.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.NamedObject;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;

/**
 * Abstract base class for all commands which process graphs
 */
abstract class BaseGraphCommand<N extends NamedObject> extends BaseTypeLoadingCommand {

	protected abstract boolean isPrintLeaves(CommandLine commandLine);
	
	protected abstract String filterCriteria(CommandLine commandLine);
	
	protected abstract Collection<N> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria);
	
	protected abstract ObjectFormatter<N> getFormatter();
	
	protected abstract void traverse(CallGraph callGraph, N namedObject, NamedObjectVisitor<N> graphPrinter);
	
	protected abstract Set<N> collectLeafObjects(CallGraph callGraph, N method);

	private void printGraph(CallGraph callGraph, PrintStream output, Collection<N> namedObjects, ObjectFormatter<N> formatter) {
		NamedObjectVisitor<N> printer = new GraphPrinter<N>(output, formatter);
		for (N namedObject : namedObjects) {
			output.println(String.format("%s:",formatter.format(namedObject)));
			traverse(callGraph, namedObject, printer);
			output.println("\n--\n");
		}
	}

	private void printLeafObjects(CallGraph callGraph, PrintStream output, Collection<N> namedObjects, ObjectFormatter<N> formatter) {
		for (N namedObject : namedObjects) {
			PrintUtil.println(output, formatter, SortUtil.sort(collectLeafObjects(callGraph, namedObject)), formatter.format(namedObject) , "\n\t", ", ");
		}
	}
	
	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		String criteria = filterCriteria(commandLine);
		boolean printLeaves = isPrintLeaves(commandLine);
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, types).build();
		Collection<N> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<N> formatter = getFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, formatter);
		} else {
			printGraph(callGraph, output, input, formatter);
		}
	}

}
