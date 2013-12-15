package org.javaan.commands;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.VertexEdgeObjectVisitor;
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

	protected abstract String filterCriteria(CommandLine commandLine);
	
	protected abstract Collection<N> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria);
	
	protected abstract ObjectFormatter<N> getFormatter();
	
	protected abstract void traverse(CallGraph callGraph, N namedObject, VertexEdgeObjectVisitor<N> graphPrinter);
	
	protected abstract Set<N> collectLeafObjects(CallGraph callGraph, N namedObject);

	protected boolean isPrintLeaves(CommandLine commandLine) {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}

	private void printGraph(CallGraph callGraph, PrintStream output, Collection<N> namedObjects, ObjectFormatter<N> formatter) {
		VertexEdgeObjectVisitor<N> printer = new GraphPrinter<N>(output, formatter);
		for (N namedObject : namedObjects) {
			output.println(String.format("%s:",formatter.format(namedObject)));
			traverse(callGraph, namedObject, printer);
			output.println(PrintUtil.BLOCK_SEPARATOR);
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
		CallGraph callGraph = new CallGraphBuilder(classContext).build();
		Collection<N> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<N> formatter = getFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, formatter);
		} else {
			printGraph(callGraph, output, input, formatter);
		}
	}
}
