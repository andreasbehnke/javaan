package org.javaan.print;

import org.javaan.model.Dependency;

public class NumberOfMethodsDependencyFormatter implements
		ObjectFormatter<Dependency> {

	@Override
	public String format(Dependency object) {
		return object.getCallees().size() + "";
	}

}
