package org.javaan;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.javaan.model.ClassData;
import org.junit.Test;

public class TestDuplicatesFinder {

	@Test
	public void testFind() {
		List<ClassData> inputClasses = new ArrayList<ClassData>();
		ClassData cd1 = mock(ClassData.class);
		when(cd1.getClassName()).thenReturn("a");
		when(cd1.getFilePath()).thenReturn("filePathA_VersionA");
		inputClasses.add(cd1);
		ClassData cd2 = mock(ClassData.class);
		when(cd2.getClassName()).thenReturn("a");
		when(cd2.getFilePath()).thenReturn("filePathA_VersionB");
		inputClasses.add(cd2);
		ClassData cd3 = mock(ClassData.class);
		when(cd3.getClassName()).thenReturn("b");
		when(cd3.getFilePath()).thenReturn("filePathB");
		inputClasses.add(cd3);
		
		List<List<ClassData>> duplicates = new DuplicatesFinder(inputClasses).find();
		assertNotNull(duplicates);
		assertEquals(1, duplicates.size());
		assertEquals(2, duplicates.get(0).size());
		assertTrue(duplicates.get(0).contains(cd1));
		assertTrue(duplicates.get(0).contains(cd2));
	}
}
