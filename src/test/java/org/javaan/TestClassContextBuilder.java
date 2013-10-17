package org.javaan;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;

public class TestClassContextBuilder implements TestConstants {

	private List<JavaClass> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
	}
	
	@Test
	public void testBuild() throws IOException {
		ClassContext context = new ClassContextBuilder(loadClasses()).build();
		Set<String> classes = context.getClasses();
		assertNotNull(classes);
		assertEquals(NUMBER_OF_CLASSES + 1, classes.size()); // + 1 because Object is always part of context
		
	}
	
}
