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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarFileLoader {

	protected final static Logger LOG = LoggerFactory.getLogger(JarFileLoader.class);

	private Type processEntry(String path, String fileName, JarFile jar, JarEntry entry) {
	    try {
            if (!entry.isDirectory()) {
                String name = entry.getName();
                boolean isClass = name.endsWith(".class");
                if (isClass) {
                    ClassParser parser = new ClassParser(fileName, entry.getName());
                    JavaClass javaClass = parser.parse();
                    String filePath = path + File.pathSeparator + javaClass.getFileName();
                    return Type.create(javaClass, filePath);
                }
            }
			return null;
        } catch (IOException ioe) {
	        throw new RuntimeException(ioe);
        }
	}
	
	private List<Type> processJar(String path, String fileName, JarFile jar) throws IOException {
	    try {
            return  jar.stream().parallel()
                    .map(jarEntry -> processEntry(path, fileName, jar, jarEntry))
                    .filter(type -> type != null)
                    .collect(Collectors.toList());
        } finally {
            jar.close();
        }
	}

	public List<Type> loadJavaClasses(String... fileNames)
			throws IOException {
		LOG.info("Processing jar files...");
        Date start = new Date();
		List<Type> types = new ArrayList<>();
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.exists()) {
				throw new IOException(String.format("JAR file %s does not exist", fileName));
			}
			JarFile jar = new JarFile(file);
			types.addAll(processJar(jar.getName(), fileName, jar));
		}
		long duration =  new Date().getTime() - start.getTime();
        LOG.info("Loading of {} class files took {} ms", types.size(), duration);
        return types;
	}
}
