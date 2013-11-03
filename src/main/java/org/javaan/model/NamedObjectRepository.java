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
	
	public void add(N objects) {
		objectMap.put(objects.getName(), objects);
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
