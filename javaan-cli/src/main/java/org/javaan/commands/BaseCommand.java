package org.javaan.commands;

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

import java.io.IOException;
import java.util.List;

import org.javaan.Command;
import org.javaan.bytecode.JarFileLoader;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand implements Command {
	
	private static final String EXCEPTION_MISSING_FILES = "No file list provided";
	
	private final static String LOGGER_NAME = "Command";

	protected final static Logger LOG = LoggerFactory.getLogger(LOGGER_NAME);

	private static final String HELP_COMMAND_LINE = "javaan %s <files> <options>";

	public BaseCommand() {
		super();
	}

	@Override
	public String getHelpCommandLine() {
		return String.format(HELP_COMMAND_LINE, getName());
	}
	
	protected List<Type> loadTypes(String[] files) throws IOException {
		if (files.length < 1) {
			System.out.println(EXCEPTION_MISSING_FILES);
			return null;
		}
		JarFileLoader loader = new JarFileLoader(true);
		List<Type> types = loader.loadJavaClasses(files);
		return types;
	}
}