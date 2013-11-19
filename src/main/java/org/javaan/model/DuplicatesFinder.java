package org.javaan.model;

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
		Set<N> testSet = new HashSet<N>();
		for (N namedObject : namedObjects) {
			if (testSet.contains(namedObject)) {
				return true;
			}
			testSet.add(namedObject);
		}
		return false;
	}

	public Set<N> createUniqueSet() {
		Set<N> unique = new HashSet<N>();
		for (N namedObject : namedObjects) {
			if (!unique.contains(namedObject)) {
				unique.add(namedObject);
			}
		}
		return unique;
	}
	
	public List<List<N>> find() {
		Map<String, List<N>> counts = new HashMap<String, List<N>>();
		for (N namedObject : namedObjects) {
			String name = namedObject.getName();
			if (counts.containsKey(name)) {
				counts.get(name).add(namedObject);
			} else {
				List<N> dups = new ArrayList<N>();
				dups.add(namedObject);
				counts.put(name, dups);
			}
		}
		List<List<N>> duplicates = new ArrayList<List<N>>();
		for (List<N> classlist : counts.values()) {
			if (classlist.size() > 1) {
				duplicates.add(classlist);
			}
		}
		return duplicates;
	}
	
}
