package org.javaan.commands;

import java.util.Collection;

import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.print.ClazzFormatter;
import org.javaan.print.ObjectFormatter;

public abstract class BaseClassDependencyGraphCommand extends BaseDependencyGraphCommand<Clazz> {

	@Override
	protected ObjectFormatter<Clazz> getTypeFormatter() {
		return new ClazzFormatter();
	}
	
	@Override
	protected Collection<Clazz> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		return SortUtil.sort(FilterUtil.filter(classContext.getClasses(), new NameMatcher<Clazz>(filterCriteria)));
	}
}
