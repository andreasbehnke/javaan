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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarFileLoader {

	protected final static Logger LOG = LoggerFactory.getLogger(JarFileLoader.class);
	
	private void processJar(String path, InputStream input, List<Type> classes) throws IOException {
		File file = File.createTempFile(new Random().nextLong() + "", ".jar");
		OutputStream output = FileUtils.openOutputStream(file);
		try {
			IOUtils.copy(input, output);
		} finally {
			output.close();
		}
		try {
			processJar(path, file.getAbsolutePath(), new JarFile(file), classes);
		} finally {
			file.delete();
		}
	}
	
	private void processEntry(String path, String fileName, JarFile jar, List<Type> classes, JarEntry entry) throws IOException {
		if (!entry.isDirectory()) {
			String name = entry.getName();
			boolean isClass = name.endsWith(".class");
			boolean isLibrary = name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear");
			if (isClass) {
				ClassParser parser = new ClassParser(fileName, entry.getName());
				JavaClass javaClass = parser.parse();
				String filePath = path + File.pathSeparator + javaClass.getFileName();
				Type type = Type.create(javaClass, filePath);
				classes.add(type);
			} else if (isLibrary) {
				InputStream input = jar.getInputStream(entry);
				try {
					processJar(path + File.pathSeparator + entry.getName(), input, classes);
				} finally {
					input.close();
				}
			}
		}
	}
	
	private void processJar(String path, String fileName, JarFile jar, List<Type> classes) throws IOException {
		try {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				processEntry(path, fileName, jar, classes, entries.nextElement());
			}
		} finally {
			jar.close();
		}
	}

	public List<Type> loadJavaClasses(String[] fileNames)
			throws IOException {
		LOG.info("Processing jar files...");
        Date start = new Date();
		List<Type> classes = new ArrayList<Type>();
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new IOException(String.format("JAR file %s does not exist", fileName));
			}
			JarFile jar = new JarFile(file);
			processJar(jar.getName(), fileName, jar, classes);
		}
		long duration =  new Date().getTime() - start.getTime();
        LOG.info("Loading of {} class files took {} ms", classes.size(), duration);
        return classes;
	}
}
