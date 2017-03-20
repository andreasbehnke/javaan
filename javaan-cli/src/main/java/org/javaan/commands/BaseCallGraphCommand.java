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
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
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

	private ObjectFormatter<Method> getFormatter() {
		return new MethodFormatter();
	}

	private Collection<Method> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getMethods(), new MethodMatcher(filterCriteria)));
	}

	private void printGraph(CallGraph callGraph, Writer writer, Collection<Method> methods,
			ObjectFormatter<Method> formatter) {
				VertexEdgeGraphVisitor<Method> printer = new VertexEdgeGraphPrinter<Method>(writer, formatter);
				for (Method method : methods) {
					PrintUtil.format(writer, "%s:",formatter.format(method));
					traverse(callGraph, method, printer);
					PrintUtil.printSeparator(writer);
				}
			}

	private void printLeafObjects(CallGraph callGraph, Writer writer, Collection<Method> methods,
			ObjectFormatter<Method> formatter) {
				for (Method method : methods) {
					PrintUtil.println(writer, formatter, SortUtil.sort(collectLeafObjects(callGraph, method)), formatter.format(method) , "\n\t", ", ");
				}
			}

	@Override
	protected void execute(CommandContext context, List<Type> types) {
		Writer writer = context.getWriter();
	    String criteria =  context.getMethodFilterCriteria();
		boolean printLeaves = context.isPrintLeaves();
		ClassContext classContext = new ClassContextBuilder().build(types);
		CallGraph callGraph = new CallGraphBuilder(
				classContext, 
				context.isResolveMethodImplementations(), 
				context.isResolveDependenciesInClassHierarchy()).build();
		Collection<Method> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<Method> formatter = getFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, writer, input, formatter);
		} else {
			printGraph(callGraph, writer, input, formatter);
		}
	}
}
