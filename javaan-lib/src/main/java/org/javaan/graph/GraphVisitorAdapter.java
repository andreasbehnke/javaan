package org.javaan.graph;

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

import org.javaan.model.GraphVisitor;
import org.jgrapht.Graph;

public abstract class GraphVisitorAdapter<V, E> implements GraphVisitor<V, E> {

	@Override
	public boolean finished() { 
		return false;
	}

	@Override
	public void visitGraph(Graph<V, E> graph, int index) {}

	@Override
	public void visitVertex(V vertex, int level) {}

	@Override
	public void visitEdge(E edge, int level) {}

}
