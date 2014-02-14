package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.model.CallGraph;
import org.javaan.model.Dependency;
import org.javaan.model.Package;
import org.jgrapht.Graph;

public class ShowPackageUsedGraph extends BasePackageDependencyGraphCommand {

	private final static String NAME = "used-packages";

	private final static String DESCRIPTION = "Display the graph of packages being used by another package. "
			+ "This is the top down view of the package dependency graph.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected void traverse(CallGraph callGraph, Package type, GraphVisitor<Package, Dependency> graphPrinter) {
		callGraph.traverseUsedPackages(type, graphPrinter);
	}

	@Override
	protected Set<Package> collectLeafObjects(CallGraph callGraph, Package type) {
		return callGraph.getLeafUsedPackages(type);
	}

	@Override
	protected Graph<Package, Dependency> getDependencyGraph(CallGraph callGraph) {
		return callGraph.getInternalGraphs().getUsageOfPackageGraph();
	}
}
