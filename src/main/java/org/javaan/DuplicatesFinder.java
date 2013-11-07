package org.javaan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javaan.model.Type;

/**
 * Finds duplicate classes within a list of {@link ClassData} objects
 */
public class DuplicatesFinder {

	private final List<Type> types;
	
	public DuplicatesFinder(List<Type> types) {
		this.types = types;
	}
	
	public List<List<Type>> find() {
		Map<String, List<Type>> counts = new HashMap<String, List<Type>>();
		for (Type type : types) {
			String className = type.getName();
			if (counts.containsKey(className)) {
				counts.get(className).add(type);
			} else {
				List<Type> dups = new ArrayList<Type>();
				dups.add(type);
				counts.put(className, dups);
			}
		}
		List<List<Type>> duplicates = new ArrayList<List<Type>>();
		for (List<Type> classlist : counts.values()) {
			if (classlist.size() > 1) {
				duplicates.add(classlist);
			}
		}
		return duplicates;
	}
	
}
