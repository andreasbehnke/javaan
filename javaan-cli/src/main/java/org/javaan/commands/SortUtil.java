package org.javaan.commands;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortUtil {

	public static <T extends Comparable<? super T>> List<T> sort(Collection<T> set) {
		List<T> sorted = new ArrayList<>(set);
		Collections.sort(sorted);
		return sorted;
	}

	/**
	 * Sort outer list by using first element of inner list
	 */
	public static <T extends Comparable<? super T>> void sort(List<List<T>> listOfClassDataList) {

		listOfClassDataList.sort((o1, o2) -> {
			// null check
			if (o1 == null && o2 == null) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
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
		});

	}
}
