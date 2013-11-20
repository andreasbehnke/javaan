package org.javaan.print;

import java.io.PrintStream;

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
	public void visit(N node, int level) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i < level; i++) {
			buffer.append(LEVEL_SPACER);
		}
		output.append(buffer).append(formatter.format(node)).println();
	}
}
