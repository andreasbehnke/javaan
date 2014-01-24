package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Method;
import org.javaan.model.Package;

public class ShowPackageUsingGraph extends BasePackageDependencyGraphCommand {

	private final static String NAME = "using-packages";

	private final static String DESCRIPTION = "Display the graph of packages using another package. "
			+ "This is the bottom up view of the package dependency graph.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Package type, GraphVisitor<Package, Method> graphPrinter) {
		callGraph.traverseUsingPackages(type, graphPrinter);
	}

	@Override
	protected Set<Package> collectLeafObjects(CallGraph callGraph, Package type) {
		return callGraph.getLeafUsingPackages(type);
	}

}
