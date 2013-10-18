package org.javaan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Finds duplicate classes within a list of {@link ClassData} objects
 */
public class DuplicatesFinder {

	private final List<ClassData> classes;
	
	public DuplicatesFinder(List<ClassData> classes) {
		this.classes = classes;
	}
	
	public List<List<ClassData>> find() {
		Map<String, List<ClassData>> counts = new HashMap<String, List<ClassData>>();
		for (ClassData classData : classes) {
			String className = classData.getClassName();
			if (counts.containsKey(className)) {
				counts.get(className).add(classData);
			} else {
				List<ClassData> dups = new ArrayList<ClassData>();
				dups.add(classData);
				counts.put(className, dups);
			}
		}
		List<List<ClassData>> duplicates = new ArrayList<List<ClassData>>();
		for (List<ClassData> classlist : counts.values()) {
			if (classlist.size() > 1) {
				duplicates.add(classlist);
			}
		}
		return duplicates;
	}
	
}
