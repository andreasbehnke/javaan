package org.javaan.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.DependencyGraphCellStyle;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Dependency;
import org.javaan.model.Type;
import org.javaan.print.MethodListDependencyFormatter;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.TypeFormatter;

public abstract class BaseClassDependencyGraphCommand extends BaseDependencyGraphCommand<Type> {

	@Override
	protected ObjectFormatter<Type> getTypeFormatter() {
		return new TypeFormatter();
	}
	
	@Override
	protected CellStyle<Type, Dependency> getDependencyGraphCellStyle() {
		return new DependencyGraphCellStyle<>(
				getTypeFormatter(), 
				new MethodListDependencyFormatter(5));
	}

	@Override
	protected Collection<Type> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		List<Type> types = new ArrayList<>();
		types.addAll(classContext.getClasses());
		types.addAll(classContext.getInterfaces());
		return SortUtil.sort(FilterUtil.filter(types, new NameMatcher<Type>(filterCriteria)));
	}

}
