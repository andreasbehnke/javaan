package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.NamedObjectVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Type;

public class ShowUsedGraph extends BaseDependencyGraphCommand {

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
	protected void traverse(CallGraph callGraph, Type namedObject, NamedObjectVisitor<Type> graphPrinter) {
		callGraph.traverseUsedTypes(namedObject, graphPrinter);
	}

	@Override
	protected Set<Type> collectLeafObjects(CallGraph callGraph, Type namedObject) {
		return callGraph.getLeafUsedTypes(namedObject);
	}	
}
