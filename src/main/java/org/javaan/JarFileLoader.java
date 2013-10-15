package org.javaan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

public class JarFileLoader {

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
			try {
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
						ClassParser parser = new ClassParser(fileName, entry.getName());
						classes.add(parser.parse());
					}
				}
			} finally {
				jar.close();
			}
		}

		return classes;
	}
}
