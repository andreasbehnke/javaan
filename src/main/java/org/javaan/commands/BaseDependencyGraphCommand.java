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
import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.graph.GraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Method;
import org.javaan.model.Type;
import org.javaan.print.ClazzFormatter;
import org.javaan.print.GraphPrinter;
import org.javaan.print.MethodFormatter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;

/**
 * Base command for all class dependency commands
 */
public abstract class BaseDependencyGraphCommand extends BaseTypeLoadingCommand {

	protected abstract void traverse(CallGraph callGraph, Clazz clazz, GraphVisitor<Clazz, Method> graphPrinter);

	protected abstract Set<Clazz> collectLeafObjects(CallGraph callGraph, Clazz clazz);
	
	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	private String filterCriteria(CommandLine commandLine) {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	private ObjectFormatter<Clazz> getClazzFormatter() {
		return new ClazzFormatter();
	}

	private ObjectFormatter<Method> getMethodFormatter() {
		return new MethodFormatter();
	}
	
	private Collection<Clazz> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getClasses(), new NameMatcher<Clazz>(filterCriteria)));
	}

	private boolean isPrintLeaves(CommandLine commandLine) {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}

	private void printGraph(CallGraph callGraph, PrintStream output, Collection<Clazz> clazzes, ObjectFormatter<Clazz> clazzFormatter, ObjectFormatter<Method> methodFormatter) {
				GraphVisitor<Clazz, Method> printer = new GraphPrinter<Clazz, Method>(output, clazzFormatter, methodFormatter);
				for (Clazz clazz : clazzes) {
					output.println(String.format("%s:",clazzFormatter.format(clazz)));
					traverse(callGraph, clazz, printer);
					output.println(PrintUtil.BLOCK_SEPARATOR);
				}
			}

	private void printLeafObjects(CallGraph callGraph, PrintStream output, Collection<Clazz> clazzes, ObjectFormatter<Clazz> formatter) {
				for (Clazz clazz : clazzes) {
					PrintUtil.println(output, formatter, SortUtil.sort(collectLeafObjects(callGraph, clazz)), formatter.format(clazz) , "\n\t", ", ");
				}
			}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		String criteria = filterCriteria(commandLine);
		boolean printLeaves = isPrintLeaves(commandLine);
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext).build();
		Collection<Clazz> input = getInput(classContext, callGraph, criteria);
		ObjectFormatter<Clazz> clazzFormatter = getClazzFormatter();
		if (printLeaves) {
			printLeafObjects(callGraph, output, input, clazzFormatter);
		} else {
			printGraph(callGraph, output, input, clazzFormatter, getMethodFormatter());
		}
	}	
}
