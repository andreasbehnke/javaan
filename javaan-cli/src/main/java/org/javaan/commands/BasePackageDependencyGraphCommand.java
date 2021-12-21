package org.javaan.commands;

import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.DependencyGraphCellStyle;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Package;
import org.javaan.print.NumberOfMethodsDependencyFormatter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PackageFormatter;

import java.util.Collection;

public abstract class BasePackageDependencyGraphCommand extends BaseDependencyGraphCommand<Package> {

	@Override
	protected ObjectFormatter<Package> getTypeFormatter() {
		return new PackageFormatter();
	}

	@Override
	protected CellStyle<Package, Dependency> getDependencyGraphCellStyle() {
		return new DependencyGraphCellStyle<>(
				getTypeFormatter(),
				new NumberOfMethodsDependencyFormatter());
	}

	@Override
	protected Collection<Package> getInput(ClassContext classContext, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getPackages(), new NameMatcher<>(filterCriteria)));
	}

}
