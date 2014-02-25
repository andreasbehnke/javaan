package org.javaan.commands;

import java.util.Set;

import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.DependencyGraphCellStyle;
import org.javaan.model.CallGraph;
import org.javaan.model.Dependency;
import org.javaan.model.GraphView;
import org.javaan.model.Package;
import org.javaan.print.NumberOfMethodsDependencyFormatter;

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
	protected CellStyle<Package, Dependency> getDependencyGraphCellStyle() {
		return new DependencyGraphCellStyle<>(
				getTypeFormatter(), 
				new NumberOfMethodsDependencyFormatter());
	}

	@Override
	protected GraphView<Package, Dependency> getDependencyGraph(CallGraph callGraph, Set<Package> filter) {
		return callGraph.getUsageOfPackageGraph(filter, false);
	}
}
