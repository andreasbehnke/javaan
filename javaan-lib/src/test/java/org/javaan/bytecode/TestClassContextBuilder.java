package org.javaan.bytecode;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Method;
import org.javaan.model.Type;
import org.junit.Test;

public class TestClassContextBuilder implements TestConstants {

	private List<Type> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(TEST_JAR_FILE);
	}

	@Test
	public void testBuild() throws IOException {
		ClassContext context = new ClassContextBuilder().build(loadClasses());

		Set<Clazz> classes = context.getClasses();
		assertNotNull(classes);
		assertEquals(NUMBER_OF_CLASSES + NUMBER_OF_EXTERNAL_CLASSES, classes.size());
		assertTrue(classes.contains(ABSTRACT_COLLECTION));

		assertEquals(SUPER_CLASS_OF_CLASS_C, context.getSuperClass(CLASS_C));

		Set<Interface> interfaces = context.getInterfaces();
		assertNotNull(interfaces);
		assertEquals(NUMBER_OF_INTERFACES + NUMBER_OF_EXTERNAL_INTERFACES, interfaces.size());

		interfaces = context.getSuperInterfaces(INTERFACE_C);
		assertNotNull(interfaces);
		assertEquals(2, interfaces.size());
		assertTrue(interfaces.contains(SUPER_INTERFACE1_OF_INTERFACE_C));
		assertTrue(interfaces.contains(SUPER_INTERFACE2_OF_INTERFACE_C));

		Set<Clazz> implementations = context.getImplementations(INTERFACE_C);
		assertNotNull(implementations);
		assertEquals(1, implementations.size());
		assertTrue(implementations.contains(CLASS_A));

		List<Method> methods =  context.getMethods(CLASS_A);
		assertNotNull(methods);
		assertEquals(2, methods.size());
		assertTrue(methods.contains(new Method(CLASS_A, SIGNATURE_METHOD_INTERFACE_B)));

		Set<Method> methods2 =  context.getVirtualMethods(CLASS_EXTENDING_EXTERNAL_CLASS);
		assertNotNull(methods2);
		assertEquals(32, methods2.size());
		assertTrue(methods2.contains(new Method(CLASS_EXTENDING_EXTERNAL_CLASS, SIGNATURE_METHOD_ABSTRACT_COLLECTION_SIZE)));
	}
}
