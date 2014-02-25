package org.javaan.print;

import org.javaan.model.Dependency;

public class ConsoleDependencyFormatter implements ObjectFormatter<Dependency> {

	@Override
	public String format(Dependency object) {
		return "- " + object.getCallees().size() + " -> ";
	}

}
