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

import org.javaan.graph.GraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;

import java.util.Set;

public class ShowCallerGraph extends BaseCallGraphCommand {

	private final static String NAME = "callers";

	private final static String DESCRIPTION = "Display the graph of methods which call another method. "
			+ "This is the bottom up view of the call graph.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Method method, GraphVisitor<Method, String> graphPrinter) {
		callGraph.traverseCallers(method, graphPrinter);
	}

	@Override
	protected Set<Method> collectLeafObjects(CallGraph callGraph, Method method) {
		return callGraph.getLeafCallers(method);
	}
}
