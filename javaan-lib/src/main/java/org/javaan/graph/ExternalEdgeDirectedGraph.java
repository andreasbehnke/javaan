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

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * Directed graph implementation which does not support edge factory,
 * the edge instance must be provided from outside.
 */
public class ExternalEdgeDirectedGraph<V, E> extends TraversalDirectedGraph<V, E> {

	private static final long serialVersionUID = 1L;

	public ExternalEdgeDirectedGraph(DirectedGraph<V, E> delegate) {
		super(delegate);
	}

	public ExternalEdgeDirectedGraph() {
		super(new DefaultDirectedGraph<V, E>(new EdgeFactory<V, E>() {

			@Override
			public E createEdge(V sourceVertex, V targetVertex) {
				throw new UnsupportedOperationException();
			}
			
		}));
	}

	@Override
	public E addEdge(V source, V target) {
		throw new UnsupportedOperationException();
	}
}
