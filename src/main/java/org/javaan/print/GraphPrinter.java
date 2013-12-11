package org.javaan.print;

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

import org.javaan.graph.NamedObjectEdge;
import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.NamedObject;

public class GraphPrinter<N extends NamedObject> implements NamedObjectVisitor<N> {

	private final static String LEVEL_SPACER = " ";
	
	private final ObjectFormatter<N> formatter;
	
	private final PrintStream output;
	
	public GraphPrinter(PrintStream output, ObjectFormatter<N> formatter) {
		this.output = output;
		this.formatter = formatter;
	}
	
	@Override
	public boolean finished() {
		return false;
	}
	
	@Override
	public void visit(N node, int level) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i < level; i++) {
			buffer.append(LEVEL_SPACER);
		}
		output.append(buffer).append(formatter.format(node)).println();
	}

	@Override
	public void visit(NamedObjectEdge<N> namedEdge, int level) {}
}
