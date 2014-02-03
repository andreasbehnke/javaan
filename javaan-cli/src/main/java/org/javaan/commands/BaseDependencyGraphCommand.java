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

import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.GraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;
import org.javaan.print.SimpleDependencyFormatter;

/**
 * Base command for all dependency commands
 */
public abstract class BaseDependencyGraphCommand<T extends Comparable<? super T>> extends BaseTypeLoadingCommand {

	protected abstract void traverse(CallGraph callGraph, T type, GraphVisitor<T, Dependency> graphPrinter);

	protected abstract Set<T> collectLeafObjects(CallGraph callGraph, T type);
	
	protected abstract ObjectFormatter<T> getTypeFormatter();
	
	protected abstract Collection<T> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria);
	
	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	protected String filterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	protected ObjectFormatter<Dependency> getDependencyFormatter() {
		return new SimpleDependencyFormatter();
	}

	protected boolean isPrintLeaves() {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}

	protected void printGraph(CallGraph callGraph, PrintStream output, Collection<T> types, ObjectFormatter<T> typeFormatter, ObjectFormatter<Dependency> dependencyFormatter) {
		GraphVisitor<T, Dependency> printer = new GraphPrinter<>(output, typeFormatter, dependencyFormatter);
		for (T type : types) {
			output.println(String.format("%s:",typeFormatter.format(type)));
			traverse(callGraph, type, printer);
			PrintUtil.printSeparator(output);
		}
	}

	protected void printLeafObjects(CallGraph callGraph, PrintStream output, Collection<T> types, ObjectFormatter<T> formatter) {
		for (T type : types) {
			PrintUtil.println(output, formatter, SortUtil.sort(collectLeafObjects(callGraph, type)), formatter.format(type) , "\n\t", ", ");
		}
	}

	@Override
	protected void execute(PrintStream output, List<Type> types) {
		String criteria = filterCriteria();
		boolean printLeaves = isPrintLeaves();
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext).build();
		Collection<T> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<T> typeFormatter = getTypeFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, typeFormatter);
		} else {
			printGraph(callGraph, output, input, typeFormatter, getDependencyFormatter());
		}
	}	
}
