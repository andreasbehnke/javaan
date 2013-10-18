package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestClassContextBuilder implements TestConstants {

	private List<ClassData> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
	}
	
	@Test
	public void testBuild() throws IOException {
		ClassContext context = new ClassContextBuilder(loadClasses()).build();
		Set<String> classes = context.getClasses();
		assertNotNull(classes);
		assertEquals(NUMBER_OF_CLASSES, classes.size());
	}
}