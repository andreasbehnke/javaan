package org.javaan.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class TestNamedObjectRepository {

	@Test( expected = IllegalArgumentException.class)
	public void testAddDuplicateObject() {
		NamedObject n1 = mock(NamedObject.class);
		when(n1.getName()).thenReturn("dup");
		NamedObject n2 = mock(NamedObject.class);
		when(n2.getName()).thenReturn("dup");
		
		NamedObjectRepository<NamedObject> rep = new NamedObjectRepository<NamedObject>();
		rep.add(n1);
		rep.add(n2);
		fail("expecting illegal argument exception");
	}
	
}
