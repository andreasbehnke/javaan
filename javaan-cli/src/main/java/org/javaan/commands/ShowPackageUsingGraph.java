package org.javaan.commands;

import java.util.Set;

import org.javaan.model.CallGraph;
import org.javaan.model.Dependency;
import org.javaan.model.GraphView;
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
	protected GraphView<Package, Dependency> getDependencyGraph(CallGraph callGraph, Set<Package> filter) {
		return callGraph.getUsageOfPackageGraph().createSubgraph(filter, true);
	}
}
