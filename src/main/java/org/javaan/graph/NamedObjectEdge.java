package org.javaan.graph;

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

import java.util.HashSet;
import java.util.Set;

import org.javaan.model.NamedObject;
import org.javaan.model.NamedObjectBase;

/**
 * Edge for {@link NamedObject} instances as vertices.
 */
public class NamedObjectEdge<V extends NamedObject> extends NamedObjectBase {

	private final V source;
	
	private final V target;

	public NamedObjectEdge(V source, V target) {
		super(buildName(source, target));
		this.source = source;
		this.target = target;
	}
	
	private static String buildName(NamedObject source, NamedObject target) {
		return source.getName() + " -> " + target.getName();
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}
	
	public static <V extends NamedObject> Set<V> getTargetSet(Set<NamedObjectEdge<V>> edges) {
		Set<V> namedObjects = new HashSet<V>();
		for (NamedObjectEdge<V> namedObjectEdge : edges) {
			namedObjects.add(namedObjectEdge.getTarget());
		}
		return namedObjects;
	}
	
	public static <V extends NamedObject> Set<V> getSourceSet(Set<NamedObjectEdge<V>> edges) {
		Set<V> namedObjects = new HashSet<V>();
		for (NamedObjectEdge<V> namedObjectEdge : edges) {
			namedObjects.add(namedObjectEdge.getSource());
		}
		return namedObjects;
	}
}
