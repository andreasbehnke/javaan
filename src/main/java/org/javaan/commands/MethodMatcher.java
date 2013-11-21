package org.javaan.commands;

import org.javaan.commands.FilterUtil.Matcher;
import org.javaan.model.Method;

public class MethodMatcher implements Matcher<Method> {
	
	private final String criteria;
	
	private boolean matchAll;
	
	public MethodMatcher(String criteria) {
		this.matchAll = (criteria == null);
		if (!matchAll) {
			this.criteria = criteria.toLowerCase();
		} else {
			this.criteria = null;
		}
	}

	@Override
	public boolean accept(Method method) {
		if (matchAll) {
			return true;
		}
		return method.getFullName().toLowerCase().contains(criteria);
	}
}
