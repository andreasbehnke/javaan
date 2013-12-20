package org.javaan.model;

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

import org.javaan.graph.GraphVisitorAdapter;
import org.javaan.graph.VertexEdge;
import org.javaan.graph.VertexEdgeGraphVisitor;

public class InterfaceMethodFinder extends GraphVisitorAdapter<Interface, VertexEdge<Interface>> implements VertexEdgeGraphVisitor<Interface> {
	
	private final String signature;
	
	private final ClassContext context;
	
	private Method methodFound = null;

	public InterfaceMethodFinder(ClassContext context, String signature) {
		this.context = context;
		this.signature = signature;
	}

	@Override
	public boolean finished() {
		return methodFound != null;
	}

	@Override
	public void visitVertex(Interface named, int level) {
		methodFound = context.getMethod(named, signature);
	}

	public Method getMethodFound() {
		return methodFound;
	}
}
