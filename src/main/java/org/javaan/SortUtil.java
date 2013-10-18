package org.javaan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SortUtil {

	public static <T extends Comparable<? super T>> List<T> sort(Set<T> set) {
		List<T> sorted = new ArrayList<T>();
		sorted.addAll(set);
		Collections.sort(sorted);
		return sorted;
	}

	/**
	 * Sort outer list by using first element of inner list
	 */
	public static <T extends Comparable<? super T>> void sort(List<List<T>> listOfClassDataList) {
		
		Collections.sort(listOfClassDataList, new Comparator<List<T>>() {
			@Override
			public int compare(List<T> o1, List<T> o2) {
				// null check
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null && o2 != null) {
					return -1;
				}
				if (o1 != null && o2 == null) {
					return 1;
				}
				
				// empty check
				int size1 = o1.size();
				int size2 = o2.size();
				if (size1 == 0 || size2 == 0) {
					if (size1 == size2) {
						return 0;
					}
					return (size1 > size2) ? 1 : -1;
				}
				
				// first element check
				return o1.get(1).compareTo(o2.get(1));
			}
		});
		
	}
}
