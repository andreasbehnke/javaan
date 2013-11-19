package org.javaan.model;

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
		List<Clazz> inputClasses = new ArrayList<Clazz>();
		Clazz c1 = new Clazz("a");
		Clazz c12 = new Clazz("a");
		Clazz c2 = new Clazz("b");
		inputClasses.add(c1);
		inputClasses.add(c12);
		inputClasses.add(c2);

		DuplicatesFinder<Clazz> finder = new DuplicatesFinder<Clazz>(inputClasses);
		List<List<Clazz>> duplicates = finder.find();
		assertNotNull(duplicates);
		assertEquals(1, duplicates.size());
		assertEquals(2, duplicates.get(0).size());
		assertTrue(duplicates.get(0).contains(c1));
		assertTrue(duplicates.get(0).contains(c12));
	}
	
	@Test
	public void testHasDuplicates() {
		List<Clazz> inputClasses = new ArrayList<Clazz>();
		Clazz c1 = new Clazz("a");
		Clazz c12 = new Clazz("a");
		Clazz c2 = new Clazz("b");
		inputClasses.add(c1);
		inputClasses.add(c12);
		inputClasses.add(c2);

		DuplicatesFinder<Clazz> finder = new DuplicatesFinder<Clazz>(inputClasses);
		assertTrue(finder.hasDuplicates());

		inputClasses = new ArrayList<Clazz>();
		inputClasses.add(c1);
		inputClasses.add(c2);

		finder = new DuplicatesFinder<Clazz>(inputClasses);
		assertFalse(finder.hasDuplicates());
	}
}
