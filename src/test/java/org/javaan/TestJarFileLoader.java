package org.javaan;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;

public class TestJarFileLoader {
	
	private static final String TEST_JAR_FILE = "src/test/resources/testJar.jar";

	@Test
	public void testLoadJarFiles() throws IOException {
		List<JavaClass> classes = new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
		assertEquals(3, classes.size());
	}
}
