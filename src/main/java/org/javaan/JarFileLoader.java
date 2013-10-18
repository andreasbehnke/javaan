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
	
	private void processJar(String path, InputStream input, List<ClassData> classes) throws IOException {
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
	
	private void processEntry(String path, String fileName, JarFile jar, List<ClassData> classes, JarEntry entry) throws IOException {
		if (!entry.isDirectory()) {
			String name = entry.getName();
			boolean isClass = name.endsWith(".class");
			boolean isLibrary = name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear");
			if (isClass) {
				ClassParser parser = new ClassParser(fileName, entry.getName());
				JavaClass clazz = parser.parse();
				ClassData data = new ClassData(path + File.pathSeparator + clazz.getFileName(), clazz);
				classes.add(data);
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
	
	private void processJar(String path, String fileName, JarFile jar, List<ClassData> classes) throws IOException {
		try {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				processEntry(path, fileName, jar, classes, entries.nextElement());
			}
		} finally {
			jar.close();
		}
	}

	public List<ClassData> loadJavaClasses(String[] fileNames)
			throws IOException {
		List<ClassData> classes = new ArrayList<ClassData>();
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new IOException(String.format(
						"JAR file %s does not exist", fileName));
			}
			JarFile jar = new JarFile(file);
			processJar(jar.getName(), fileName, jar, classes);
		}

		return classes;
	}
}
