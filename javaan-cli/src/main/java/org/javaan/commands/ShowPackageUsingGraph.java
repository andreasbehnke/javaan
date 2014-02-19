package org.javaan.commands;

import java.util.Set;

import org.javaan.graph.GraphVisitor;
import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.DependencyGraphCellStyle;
import org.javaan.model.CallGraph;
import org.javaan.model.Dependency;
import org.javaan.model.Package;
import org.javaan.print.NumberOfMethodsDependencyFormatter;
import org.jgrapht.Graph;
import org.jgrapht.graph.EdgeReversedGraph;

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
	protected void traverse(CallGraph callGraph, Package type, GraphVisitor<Package, Dependency> graphPrinter) {
		callGraph.traverseUsingPackages(type, graphPrinter);
	}

	@Override
	protected Set<Package> collectLeafObjects(CallGraph callGraph, Package type) {
		return callGraph.getLeafUsingPackages(type);
	}

	@Override
	protected CellStyle<Package, Dependency> getDependencyGraphCellStyle() {
		return new DependencyGraphCellStyle<>(
				getTypeFormatter(), 
				new NumberOfMethodsDependencyFormatter());
	}
	
	@Override
	protected Graph<Package, Dependency> getDependencyGraph(CallGraph callGraph) {
		return new EdgeReversedGraph<>(callGraph.getInternalGraphs().getUsageOfPackageGraph());
	}
}
