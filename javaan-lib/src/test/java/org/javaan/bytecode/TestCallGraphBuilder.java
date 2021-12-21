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

import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Method;
import org.javaan.model.Type;
import org.junit.Test;

public class TestCallGraphBuilder implements TestConstants {

	private List<Type> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(TEST_JAR_FILE);
	}

	@Test
	public void testBuild() throws IOException {
		List<Type> classes = loadClasses();

		ClassContext classContext = new ClassContextBuilder().build(classes);
		CallGraph callGraph = new CallGraphBuilder(classContext, true, false).build();

		assertNotNull(callGraph);

		// class calling interface method
		Set<Method> callers = callGraph.getCallers(CLASSA_METHOD_INTERFACE_B);
		assertNotNull(callers);
		assertEquals(2, callers.size());
		assertTrue(callers.contains(CLASSB_METHOD_CLASS_B));
		assertTrue(callers.contains(CLASSC_ENTRY_METHOD));

		// callee of method
		Set<Method> callee = callGraph.getCallee(CLASSC_ENTRY_METHOD);
		assertNotNull(callee);
		assertEquals(3, callee.size());
		assertTrue(callee.contains(CLASSB_METHOD_CLASS_B));
		assertTrue(callee.contains(CLASSA_METHOD_INTERFACE_B));
		assertTrue(callee.contains(CLASSB_CONSTRUCTOR));

		// abstract method call
		callee = callGraph.getCallee(CLASS_CALLING_ABSTACT_METHOD_CALL_ABSTRACT_METHOD);
		assertNotNull(callee);
		assertEquals(1, callee.size());
		assertTrue(callee.contains(SPECIALIZATION_CLASS_B_ABSTRACT_METHOD));

		// external method call (String constructor)
		assertTrue(classContext.getMethods().contains(CLASSC_CALLING_EXTERNAL_CLASS));
		callers = callGraph.getCallers(STRING_CONSTRUCTOR);
		assertNotNull(callers);
		assertEquals(1, callers.size());
		assertTrue(callers.contains(CLASSC_CALLING_EXTERNAL_CLASS));
 	}
}
