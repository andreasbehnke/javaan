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
