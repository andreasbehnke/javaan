package org.javaan.commands;

import java.util.Collection;

import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Package;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PackageFormatter;
import org.jgrapht.Graph;

public abstract class BasePackageDependencyGraphCommand extends BaseDependencyGraphCommand<Package> {

	@Override
	protected ObjectFormatter<Package> getTypeFormatter() {
		return new PackageFormatter();
	}

	@Override
	protected Collection<Package> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getPackages(), new NameMatcher<Package>(filterCriteria)));
	}

	@Override
	protected Graph<Package, Dependency> getDependencyGraph(CallGraph callGraph) {
		return callGraph.getInternalGraphs().getUsageOfPackageGraph();
	}
}
