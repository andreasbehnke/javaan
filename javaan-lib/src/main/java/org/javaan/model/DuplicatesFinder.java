package org.javaan.model;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Finds duplicate objects within a list of {@link NamedObject}s
 */
public class DuplicatesFinder<N extends NamedObject> {

	private final List<N> namedObjects;

	public DuplicatesFinder(List<N> namedObjects) {
		this.namedObjects = namedObjects;
	}

	public boolean hasDuplicates() {
		Set<N> testSet = new HashSet<>();
		for (N namedObject : namedObjects) {
			if (testSet.contains(namedObject)) {
				return true;
			}
			testSet.add(namedObject);
		}
		return false;
	}

	public Set<N> createUniqueSet() {
		return new HashSet<>(namedObjects);
	}

	public List<List<N>> find() {
		Map<String, List<N>> counts = new HashMap<>();
		for (N namedObject : namedObjects) {
			String name = namedObject.getName();
			if (counts.containsKey(name)) {
				counts.get(name).add(namedObject);
			} else {
				List<N> dups = new ArrayList<>();
				dups.add(namedObject);
				counts.put(name, dups);
			}
		}
		List<List<N>> duplicates = new ArrayList<>();
		for (List<N> classList : counts.values()) {
			if (classList.size() > 1) {
				duplicates.add(classList);
			}
		}
		return duplicates;
	}

}
