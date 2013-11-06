package org.javaan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javaan.model.NamedObject;

public class FilterUtil {
	
	public static <E extends NamedObject> List<E> filter(Collection<E> list, String criteria) {
		List<E> filtered = new ArrayList<E>();
		for (E namedObject : list) {
			if (namedObject.getName().toLowerCase().contains(criteria.toLowerCase())) {
				filtered.add(namedObject);
			}
		}
		return filtered;
	}

}
