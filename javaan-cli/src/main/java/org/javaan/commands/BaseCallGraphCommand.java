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
import org.javaan.graph.VertexEdgeGraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Method;
import org.javaan.model.Type;
import org.javaan.print.MethodFormatter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;
import org.javaan.print.VertexEdgeGraphPrinter;

/**
 * Base command for all method call graph commands
 */
public abstract class BaseCallGraphCommand extends BaseTypeLoadingCommand {

	protected abstract void traverse(CallGraph callGraph, Method method, VertexEdgeGraphVisitor<Method> graphPrinter);

	protected abstract Set<Method> collectLeafObjects(CallGraph callGraph, Method method);
	
	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.METHOD);
		options.addOption(StandardOptions.LEAVES);
		options.addOption(StandardOptions.RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
		options.addOption(StandardOptions.RESOLVE_METHOD_IMPLEMENTATIONS);
		return options;
	}

	private String filterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_METHOD);
	}
	
	private boolean resolveDependenciesInClassHierarchy() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
	}
	
	private boolean resolveMethodImplementations() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS);
	}

	private boolean isPrintLeaves() {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}

	private ObjectFormatter<Method> getFormatter() {
		return new MethodFormatter();
	}

	private Collection<Method> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(filterCriteria)));
	}

	private void printGraph(CallGraph callGraph, PrintStream output, Collection<Method> methods,
			ObjectFormatter<Method> formatter) {
				VertexEdgeGraphVisitor<Method> printer = new VertexEdgeGraphPrinter<Method>(output, formatter);
				for (Method method : methods) {
					output.println(String.format("%s:",formatter.format(method)));
					traverse(callGraph, method, printer);
					PrintUtil.printSeparator(output);
				}
			}

	private void printLeafObjects(CallGraph callGraph, PrintStream output, Collection<Method> methods,
			ObjectFormatter<Method> formatter) {
				for (Method method : methods) {
					PrintUtil.println(output, formatter, SortUtil.sort(collectLeafObjects(callGraph, method)), formatter.format(method) , "\n\t", ", ");
				}
			}

	@Override
	protected void execute(PrintStream output, List<Type> types) {
		String criteria = filterCriteria();
		boolean printLeaves = isPrintLeaves();
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(
				classContext, 
				resolveMethodImplementations(), 
				resolveDependenciesInClassHierarchy()).build();
		Collection<Method> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<Method> formatter = getFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, formatter);
		} else {
			printGraph(callGraph, output, input, formatter);
		}
	}
}
