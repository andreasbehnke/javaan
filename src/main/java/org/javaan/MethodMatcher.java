package org.javaan;

import org.javaan.FilterUtil.Matcher;
import org.javaan.model.Method;

public class MethodMatcher implements Matcher<Method> {
	
	private final String criteria;
	
	public MethodMatcher(String criteria) {
		this.criteria = criteria.toLowerCase();
	}

	@Override
	public boolean accept(Method method) {
		return method.getFullName().toLowerCase().contains(criteria);
	}
}
