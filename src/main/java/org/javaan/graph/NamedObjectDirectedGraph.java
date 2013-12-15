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

import org.javaan.model.NamedObject;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class NamedObjectDirectedGraph<V extends NamedObject> 
	extends TraversalDirectedGraph<V, VertexEdge<V>> 
	implements DirectedGraph<V, VertexEdge<V>> {

	private static final long serialVersionUID = 1L;

	public NamedObjectDirectedGraph() {
		super(new DefaultDirectedGraph<V, VertexEdge<V>>(new VertexEdgeFactory<V>()));
	}

}