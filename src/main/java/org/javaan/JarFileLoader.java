package org.javaan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class JarFileLoader {
	
	private void processJar(InputStream input, List<JavaClass> classes) throws IOException {
		File file = File.createTempFile(new Random().nextLong() + "", ".jar");
		OutputStream output = FileUtils.openOutputStream(file);
		try {
			IOUtils.copy(input, output);
		} finally {
			output.close();
		}
		try {
			processJar(file.getAbsolutePath(), new JarFile(file), classes);
		} finally {
			file.delete();
		}
	}
	
	private void processEntry(String fileName, JarFile jar, List<JavaClass> classes, JarEntry entry) throws IOException {
		if (!entry.isDirectory()) {
			String name = entry.getName();
			boolean isClass = name.endsWith(".class");
			boolean isLibrary = name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear");
			if (isClass) {
				ClassParser parser = new ClassParser(fileName, entry.getName());
				classes.add(parser.parse());
			} else if (isLibrary) {
				InputStream input = jar.getInputStream(entry);
				try {
					processJar(input, classes);
				} finally {
					input.close();
				}
			}
		}
	}
	
	private void processJar(String fileName, JarFile jar, List<JavaClass> classes) throws IOException {
		try {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				processEntry(fileName, jar, classes, entries.nextElement());
			}
		} finally {
			jar.close();
		}
	}

	public List<JavaClass> loadJavaClasses(String[] fileNames)
			throws IOException {
		List<JavaClass> classes = new ArrayList<JavaClass>();
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new IOException(String.format(
						"JAR file %s does not exist", fileName));
			}
			JarFile jar = new JarFile(file);
			processJar(fileName, jar, classes);
		}

		return classes;
	}
}
