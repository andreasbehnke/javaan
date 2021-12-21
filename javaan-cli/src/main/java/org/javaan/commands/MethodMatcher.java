package org.javaan.commands;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.javaan.commands.FilterUtil.Matcher;
import org.javaan.model.Method;

public class MethodMatcher implements Matcher<Method> {

	private final static String PREFIX_ANNOTATION = "a:";

	private final String criteria;

	private final boolean matchAll;

	private boolean matchAnnotation = false;

	public MethodMatcher(String criteria) {
		this.matchAll = (criteria == null);
		if (!matchAll) {
			if (criteria.startsWith(PREFIX_ANNOTATION)) {
				matchAnnotation = true;
				this.criteria = criteria.substring(PREFIX_ANNOTATION.length()).toLowerCase();
			} else {
				this.criteria = criteria.toLowerCase();
			}
		} else {
			this.criteria = null;
		}
	}

	@Override
	public boolean accept(Method method) {
		if (matchAll) {
			return true;
		}
		if (matchAnnotation) {
			return acceptAnnotationClass(method);
		} else {
			return acceptMethodName(method);
		}
	}

	private boolean acceptMethodName(Method method) {
		boolean match = method.getName().toLowerCase().contains(criteria);
		if (!match) {
			match = method.getType().getName().toLowerCase().contains(criteria);
		}
		return match;
	}

	private boolean acceptAnnotationClass(Method method) {
		for (String annotation : method.getAnnotationTypes()) {
			boolean match = annotation.toLowerCase().contains(criteria);
			if (match) {
				return true;
			}
		}
		return false;
	}
}
