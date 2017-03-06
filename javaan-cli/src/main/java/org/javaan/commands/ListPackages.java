package org.javaan.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Package;
import org.javaan.model.Type;
import org.javaan.print.PrintUtil;

public class ListPackages extends BaseTypeLoadingCommand {

	private final static String NAME = "packages";
	
	private final static String DESCRIPTION = "List all packages of the libraries";
	
	private ClassContext classContext;
	
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
	protected void execute(PrintStream output, CommandContext context, List<Type> types) {
		this.classContext = new ClassContextBuilder(types).build();
		boolean isTopologicalSort = context.isTopologicalSort();
		Collection<Package> packages = null;
		if (isTopologicalSort) {
			packages = new CallGraphBuilder(this.classContext, context.isResolveMethodImplementations(), context.isResolveDependenciesInClassHierarchy())
					.build().getTopologicalSortedPackages();
		} else {
			packages = classContext.getPackages();
		}
		if (context.hasFilterCriteria()) {
			String criteria = context.getFilterCriteria();
			packages = FilterUtil.filter(packages, new NameMatcher<Package>(criteria)); 
		}
		if (!isTopologicalSort) {
			packages = SortUtil.sort(packages);
		}
		PrintUtil.println(output, packages, "", "[P]", System.lineSeparator());
	}

}
