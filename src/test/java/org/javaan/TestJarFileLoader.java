package org.javaan;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class TestJarFileLoader implements TestConstants {
	
	@Test
	public void testLoadJarFiles() throws IOException {
		List<ClassData> classes = new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
		assertEquals(NUMBER_OF_CLASSES_AND_INTERFACES, classes.size());
	}
}
