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

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.Graph2dDisplay;
import org.javaan.ReturnCodes;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.GraphVisitor;
import org.javaan.jgraphx.CellStyle;
import org.javaan.model.*;
import org.javaan.print.ConsoleDependencyFormatter;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;

import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base command for all dependency commands
 */
public abstract class BaseDependencyGraphCommand<T extends Comparable<? super T>> extends BaseTypeLoadingCommand {

	protected abstract GraphView<T,	Dependency> getDependencyGraph(CallGraph callGraph, Set<T> filter);

	protected abstract ObjectFormatter<T> getTypeFormatter();

	protected abstract Collection<T> getInput(ClassContext classContext, String filterCriteria);

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

	private ObjectFormatter<Dependency> getConsoleDependencyFormatter() {
		return new ConsoleDependencyFormatter();
	}

	protected abstract CellStyle<T, Dependency> getDependencyGraphCellStyle();

	private void printGraph(CallGraph callGraph, Writer writer, Collection<T> types, ObjectFormatter<T> typeFormatter, ObjectFormatter<Dependency> dependencyFormatter) {
		GraphVisitor<T, Dependency> printer = new GraphPrinter<>(writer, typeFormatter, dependencyFormatter);
		Set<T> filter = new HashSet<>(types);
		GraphView<T, Dependency> graphView = getDependencyGraph(callGraph, filter);
		for (T type : types) {
			PrintUtil.format(writer,"%s:",typeFormatter.format(type));
			graphView.traverseDepthFirst(type, printer, false);
			PrintUtil.printSeparator(writer);
		}
	}

	private void printLeafObjects(CallGraph callGraph, Writer writer, Collection<T> types, ObjectFormatter<T> formatter) {
		GraphView<T, Dependency> graphView = getDependencyGraph(callGraph, null);
		for (T type : types) {
			PrintUtil.println(writer, formatter, SortUtil.sort(graphView.collectLeaves(type, false)), formatter.format(type) , "\n\t", ", ");
		}
	}

	@Override
	protected void execute(final CommandContext context, List<Type> types) {
        Writer writer = context.getWriter();
        String criteria = context.getFilterCriteria();
		boolean printLeaves = context.isPrintLeaves();
		ClassContext classContext = new ClassContextBuilder().build(types);
		CallGraph callGraph = new CallGraphBuilder(
				classContext,
				context.isResolveMethodImplementations(),
				context.isResolveDependenciesInClassHierarchy()).build();
		Collection<T> input = getInput(classContext, criteria);
		final ObjectFormatter<T> typeFormatter = getTypeFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, writer, input, typeFormatter);
		} else if (context.isDisplay2dGraph())  {
			Set<T> filter = new HashSet<>(input);
			final GraphView<T, Dependency> graph = getDependencyGraph(callGraph, filter);
			java.awt.EventQueue.invokeLater(() -> {
				CellStyle<T, Dependency> cellStyle = getDependencyGraphCellStyle();
				new Graph2dDisplay<>(getName(), graph, cellStyle, context.getSettings()).setVisible(true);
			});
			context.setReturnCode(ReturnCodes.threadSpawn);
		} else {
			printGraph(callGraph, writer, input, typeFormatter, getConsoleDependencyFormatter());
		}
	}
}
