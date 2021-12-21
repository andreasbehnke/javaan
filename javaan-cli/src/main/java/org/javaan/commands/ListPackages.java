package org.javaan.commands;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Package;
import org.javaan.model.Type;
import org.javaan.print.PrintUtil;

import java.util.Collection;
import java.util.List;

public class ListPackages extends BaseTypeLoadingCommand {

	private final static String NAME = "packages";

	private final static String DESCRIPTION = "List all packages of the libraries";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.TOPOLOGICAL_SORT);
		options.addOption(StandardOptions.RESOLVE_METHOD_IMPLEMENTATIONS);
		options.addOption(StandardOptions.RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
		return options;
	}

	@Override
	protected void execute(CommandContext context, List<Type> types) {
		ClassContext classContext = new ClassContextBuilder().build(types);
		boolean isTopologicalSort = context.isTopologicalSort();
		Collection<Package> packages;
		if (isTopologicalSort) {
			packages = new CallGraphBuilder(classContext, context.isResolveMethodImplementations(), context.isResolveDependenciesInClassHierarchy())
					.build().getTopologicalSortedPackages();
		} else {
			packages = classContext.getPackages();
		}
		if (context.hasFilterCriteria()) {
			String criteria = context.getFilterCriteria();
			packages = FilterUtil.filter(packages, new NameMatcher<>(criteria));
		}
		if (!isTopologicalSort) {
			packages = SortUtil.sort(packages);
		}
		PrintUtil.println(context.getWriter(), packages, "", "[P]", System.lineSeparator());
	}

}
