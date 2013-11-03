package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.javaan.model.Type;
import org.junit.Test;

public class TestDuplicatesFinder {

	@Test
	public void testFind() {
		List<Type> inputClasses = new ArrayList<Type>();
		Type cd1 = mock(Type.class);
		when(cd1.getName()).thenReturn("a");
		when(cd1.getFilePath()).thenReturn("filePathA_VersionA");
		inputClasses.add(cd1);
		Type cd2 = mock(Type.class);
		when(cd2.getName()).thenReturn("a");
		when(cd2.getFilePath()).thenReturn("filePathA_VersionB");
		inputClasses.add(cd2);
		Type cd3 = mock(Type.class);
		when(cd3.getName()).thenReturn("b");
		when(cd3.getFilePath()).thenReturn("filePathB");
		inputClasses.add(cd3);
		
		List<List<Type>> duplicates = new DuplicatesFinder(inputClasses).find();
		assertNotNull(duplicates);
		assertEquals(1, duplicates.size());
		assertEquals(2, duplicates.get(0).size());
		assertTrue(duplicates.get(0).contains(cd1));
		assertTrue(duplicates.get(0).contains(cd2));
	}
}
