package org.javaan.bytecode;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.javaan.bytecode.JarFileLoader;
import org.javaan.model.Type;
import org.junit.Test;

public class TestJarFileLoader implements TestConstants {
	
	@Test
	public void testLoadJarFiles() throws IOException {
		List<Type> types = new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
		assertEquals(NUMBER_OF_CLASSES_AND_INTERFACES, types.size());
	}
}
