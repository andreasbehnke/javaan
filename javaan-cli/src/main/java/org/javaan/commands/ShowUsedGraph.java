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

import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.DependencyGraphCellStyle;
import org.javaan.model.CallGraph;
import org.javaan.model.Dependency;
import org.javaan.model.Type;
import org.javaan.print.MethodListDependencyFormatter;
import org.jgrapht.Graph;

public class ShowUsedGraph extends BaseClassDependencyGraphCommand {

	private final static String NAME = "used";

	private final static String DESCRIPTION = "Display the graph of classes being used by another class. "
			+ "This is the top down view of the class dependency graph.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Type type, GraphVisitor<Type, Dependency> graphPrinter) {
		callGraph.traverseUsedTypes(type, graphPrinter);
	}

	@Override
	protected Set<Type> collectLeafObjects(CallGraph callGraph, Type type) {
		return callGraph.getLeafUsedTypes(type);
	}

	@Override
	protected CellStyle<Type, Dependency> getDependencyGraphCellStyle() {
		return new DependencyGraphCellStyle<>(
				getTypeFormatter(), 
				new MethodListDependencyFormatter(5));
	}

	@Override
	protected Graph<Type, Dependency> getDependencyGraph(CallGraph callGraph) {
		return callGraph.getInternalGraphs().getUsageOfTypeGraph();
	}
}
