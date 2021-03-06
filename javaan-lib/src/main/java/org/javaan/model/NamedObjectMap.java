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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NamedObjectMap<N extends NamedObject> extends HashMap<String, N> implements NamedObjectRepository<N> {
	
	public NamedObjectMap() {}
	
	public NamedObjectMap(Collection<N> objects) {
		addAll(objects);
	}
	
	public void add(N object) {
		String name = object.getName();
		if (containsKey(name)) {
			throw new IllegalArgumentException("Duplicate object found: " + object.getName());
		}
		put(name, object);
	}
	
	public void addAll(Collection<N> objects) {
		for (N object : objects) {
			add(object);
		}
	}

	@Override
	public N get(String name) {
		return super.get(name);
	}

	public Collection<N> getNamedObjects() {
		return values();
	}
	
	public boolean contains(String name) {
		return containsKey(name);
	}

}
