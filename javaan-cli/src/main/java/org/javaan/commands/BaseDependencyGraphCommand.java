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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.Graph2dDisplay;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.GraphFilter;
import org.javaan.graph.GraphVisitor;
import org.javaan.graph.UnsupportedEdgeFactory;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;
import org.javaan.print.SimpleDependencyFormatter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * Base command for all dependency commands
 */
public abstract class BaseDependencyGraphCommand<T extends Comparable<? super T>> extends BaseTypeLoadingCommand {

	protected abstract void traverse(CallGraph callGraph, T type, GraphVisitor<T, Dependency> graphPrinter);
	
	protected abstract Graph<T,	Dependency> getDependencyGraph(CallGraph callGraph);

	protected abstract Set<T> collectLeafObjects(CallGraph callGraph, T type);
	
	protected abstract ObjectFormatter<T> getTypeFormatter();
	
	protected abstract Collection<T> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria);
	
	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		OptionGroup outputVariations = new OptionGroup();
		outputVariations.addOption(StandardOptions.LEAVES);
		outputVariations.addOption(StandardOptions.DISPLAY_2D_GRAPH);
		options.addOptionGroup(outputVariations);
		options.addOption(StandardOptions.RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
		options.addOption(StandardOptions.RESOLVE_METHOD_IMPLEMENTATIONS);
		return options;
	}

	private String filterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}
	
	private boolean resolveDependenciesInClassHierarchy() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
	}
	
	private boolean resolveMethodImplementations() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS);
	}

	private boolean printLeaves() {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}
	
	private boolean display2dGraph() {
		return commandLine.hasOption(StandardOptions.OPT_DISPLAY_2D_GRAPH);
	}
	
	private ObjectFormatter<Dependency> getDependencyFormatter() {
		return new SimpleDependencyFormatter();
	}

	private void printGraph(CallGraph callGraph, PrintStream output, Collection<T> types, ObjectFormatter<T> typeFormatter, ObjectFormatter<Dependency> dependencyFormatter) {
		GraphVisitor<T, Dependency> printer = new GraphPrinter<>(output, typeFormatter, dependencyFormatter);
		for (T type : types) {
			output.println(String.format("%s:",typeFormatter.format(type)));
			traverse(callGraph, type, printer);
			PrintUtil.printSeparator(output);
		}
	}

	private void printLeafObjects(CallGraph callGraph, PrintStream output, Collection<T> types, ObjectFormatter<T> formatter) {
		for (T type : types) {
			PrintUtil.println(output, formatter, SortUtil.sort(collectLeafObjects(callGraph, type)), formatter.format(type) , "\n\t", ", ");
		}
	}

	@Override
	protected void execute(PrintStream output, List<Type> types) {
		String criteria = filterCriteria();
		boolean printLeaves = printLeaves();
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(
				classContext, 
				resolveMethodImplementations(), 
				resolveDependenciesInClassHierarchy()).build();
		Collection<T> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<T> typeFormatter = getTypeFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, typeFormatter);
		} else if (display2dGraph())  {
			Set<T> filter = new HashSet<>(input);
			Graph<T, Dependency> graph = getDependencyGraph(callGraph);
			final Graph<T, Dependency> subgraph = new  GraphFilter<>(
					graph, new DefaultDirectedGraph<>(new UnsupportedEdgeFactory<T, Dependency>())).filter(filter);
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new Graph2dDisplay(subgraph, getName()).setVisible(true);
				}
			});
		} else {
			printGraph(callGraph, output, input, typeFormatter, getDependencyFormatter());
		}
	}	
}
