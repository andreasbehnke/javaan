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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestDuplicatesFinder {

	@Test
	public void testFind() {
		List<Clazz> inputClasses = new ArrayList<>();
		Clazz c1 = new Clazz("a");
		Clazz c12 = new Clazz("a");
		Clazz c2 = new Clazz("b");
		inputClasses.add(c1);
		inputClasses.add(c12);
		inputClasses.add(c2);

		DuplicatesFinder<Clazz> finder = new DuplicatesFinder<>(inputClasses);
		List<List<Clazz>> duplicates = finder.find();
		assertNotNull(duplicates);
		assertEquals(1, duplicates.size());
		assertEquals(2, duplicates.get(0).size());
		assertTrue(duplicates.get(0).contains(c1));
		assertTrue(duplicates.get(0).contains(c12));
	}

	@Test
	public void testHasDuplicates() {
		List<Clazz> inputClasses = new ArrayList<>();
		Clazz c1 = new Clazz("a");
		Clazz c12 = new Clazz("a");
		Clazz c2 = new Clazz("b");
		inputClasses.add(c1);
		inputClasses.add(c12);
		inputClasses.add(c2);

		DuplicatesFinder<Clazz> finder = new DuplicatesFinder<>(inputClasses);
		assertTrue(finder.hasDuplicates());

		inputClasses = new ArrayList<>();
		inputClasses.add(c1);
		inputClasses.add(c2);

		finder = new DuplicatesFinder<>(inputClasses);
		assertFalse(finder.hasDuplicates());
	}
}
