package org.javaan.commands;

import org.javaan.commands.FilterUtil.Matcher;
import org.javaan.model.NamedObject;

public class NameMatcher<E extends NamedObject> implements Matcher<E> {
	
	private final String criteria;

	public NameMatcher(String criteria) {
		this.criteria = criteria.toLowerCase();
	}

	@Override
	public boolean accept(E e) {
		return e.getName().toLowerCase().contains(criteria);
	}
}
