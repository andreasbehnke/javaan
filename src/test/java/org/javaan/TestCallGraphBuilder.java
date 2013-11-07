package org.javaan;

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
		return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
	}
	
	@Test
	public void testBuild() throws IOException {
		List<Type> classes = loadClasses();
		
		ClassContext classContext = new ClassContextBuilder(classes).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, classes).build();
		
		assertNotNull(callGraph);
		
		Set<Method> callers = callGraph.getCallers(CLASSA_METHOD_INTERFACE_B);
		assertNotNull(callers);
		assertEquals(2, callers.size());
		assertTrue(callers.contains(CLASSB_METHOD_CLASS_B));
		assertTrue(callers.contains(CLASSC_ENTRY_METHOD));
		
		Set<Method> callees = callGraph.getCallees(CLASSC_ENTRY_METHOD);
		assertNotNull(callees);
		assertEquals(3, callees.size());
		assertTrue(callees.contains(CLASSB_METHOD_CLASS_B));
		assertTrue(callees.contains(CLASSA_METHOD_INTERFACE_B));
		assertTrue(callees.contains(CLASSB_CONSTRUCTOR));
 	}
}
