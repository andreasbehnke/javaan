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
