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
import java.util.List;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Type;
import org.javaan.print.GraphPrinter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.ConsoleDependencyFormatter;
import org.javaan.print.TypeFormatter;

public class ShowDepdendencyCyclesGraph extends BaseTypeLoadingCommand {

	private final static String NAME = "cycles";
	
	private final static String DESCRIPTION = "Show call graph for each dependency cycle in the loaded libraries. Cycles within class hierachies are omitted, if option -rdh is not provided.";
	
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
		options.addOption(StandardOptions.RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
		options.addOption(StandardOptions.RESOLVE_METHOD_IMPLEMENTATIONS);
		return options;
	}

	@Override
	protected void execute(CommandContext context, List<Type> types) {
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(
				classContext, 
				context.isResolveMethodImplementations(), 
				context.isResolveDependenciesInClassHierarchy()).build();
		ObjectFormatter<Type> typeFormatter = new TypeFormatter();
		ObjectFormatter<Dependency> dependencyFormatter = new ConsoleDependencyFormatter();
		GraphPrinter<Type, Dependency> printer = new GraphPrinter<Type, Dependency>(context.getWriter(), typeFormatter, dependencyFormatter, "cycle %s:");
		callGraph.traverseDependencyCycles(printer);
	}
}
