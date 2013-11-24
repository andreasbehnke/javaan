package org.javaan.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterUtil {
	
	public static interface Matcher<E> {
		boolean accept(E e);
	}
	
	public static <E> List<E> filter(Collection<E> list, Matcher<E> matcher) {
		List<E> filtered = new ArrayList<E>();
		for (E e : list) {
			if (matcher.accept(e)) {
				filtered.add(e);
			}
		}
		return filtered;
	}

}
