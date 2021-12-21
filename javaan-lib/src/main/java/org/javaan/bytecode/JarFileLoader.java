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

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarFileLoader {

	protected final static Logger LOG = LoggerFactory.getLogger(JarFileLoader.class);

	private final List<File> tempFiles = new ArrayList<>();

	private static Type parse(String path, JarFile jar, JarEntry entry) {
		try {
			ClassParser parser = new ClassParser(jar.getInputStream(entry), entry.getName());
			JavaClass javaClass = parser.parse();
			String filePath = path + File.pathSeparator + javaClass.getFileName();
			return Type.create(javaClass, filePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Type> processJar(JarFile jar) {
		return  jar.stream().parallel()
				.filter(jarEntry -> jarEntry.getName().endsWith(".class"))
				.map(jarEntry -> parse(jar.getName(), jar, jarEntry))
				.collect(Collectors.toList());
	}

	private JarFile createTempJarFile(JarFile parent, JarEntry jarEntry) {
		try (InputStream inputStream = parent.getInputStream(jarEntry)) {
			File tempFile = File.createTempFile(parent.getName() + "." + jarEntry.getName(), ".jar");
			FileUtils.copyInputStreamToFile(inputStream, tempFile);
			tempFiles.add(tempFile);
			return new JarFile(tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Type> processJarsInJar(JarFile jar) {
		return jar.stream().parallel()
				.filter(jarEntry -> jarEntry.getName().endsWith(".jar"))
				.map(jarEntry -> createTempJarFile(jar, jarEntry))
				.map(this::processJar)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public List<Type> loadJavaClasses(String... fileNames)
			throws IOException {
		LOG.info("Processing jar files...");
        Date start = new Date();
		List<Type> types = new ArrayList<>();
		this.tempFiles.clear();
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new IOException(String.format("JAR file %s does not exist", fileName));
			}
			JarFile jar = new JarFile(file);
			types.addAll(processJarsInJar(jar));
			tempFiles.stream().forEach(File::delete);
			types.addAll(processJar(jar));
		}
		long duration =  new Date().getTime() - start.getTime();
        LOG.info("Loading of {} class files took {} ms", types.size(), duration);
        return types;
	}
}
