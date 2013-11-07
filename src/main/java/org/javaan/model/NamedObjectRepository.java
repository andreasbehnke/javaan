package org.javaan.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NamedObjectRepository<N extends NamedObject> {
	
	private final Map<String, N> objectMap = new HashMap<String, N>();
	
	public NamedObjectRepository() {}
	
	public NamedObjectRepository(Collection<N> objects) {
		addAll(objects);
	}
	
	public void add(N object) {
		String name = object.getName();
		if (objectMap.containsKey(name)) {
			throw new IllegalArgumentException("Duplicate object found: " + object.getName());
		}
		objectMap.put(name, object);
	}
	
	public void addAll(Collection<N> objects) {
		for (N object : objects) {
			add(object);
		}
	}
	
	public N get(String name) {
		return objectMap.get(name);
	}

	public Collection<N> getNamedObjects() {
		return objectMap.values();
	}
}
