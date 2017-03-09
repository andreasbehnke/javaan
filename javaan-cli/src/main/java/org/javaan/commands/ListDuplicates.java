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
import java.io.Writer;
import java.util.List;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.model.DuplicatesFinder;
import org.javaan.model.Type;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.PrintUtil;
import org.javaan.print.TypeInformationFormatter;

public class ListDuplicates extends BaseCommand {
	
private final static String NAME = "duplicates";
	
	private final static String DESCRIPTION = "List all duplicate classes and interfaces of the libraries being loaded. "
			+ "Classes are marked as duplications if they share canonical name, they may vary in bytecode and file location.";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Options buildCommandLineOptions(Options options) {
		return options;
	}
	
	@Override
	public ReturnCodes execute(CommandContext context) {
		try {
			List<Type> types = loadTypes(context.getArguments());
			if(types == null) {
				return ReturnCodes.errorParse;
			}
			printDuplicates(context.getWriter(), types);
		} catch (IOException e) {
			LOG.error("Could not load class files from libraries", e);
			return ReturnCodes.errorCommand;
		}
		return ReturnCodes.ok;
	}

	private void printDuplicates(Writer output, List<Type> types) {
		List<List<Type>> duplicates = new DuplicatesFinder<Type>(types).find();
		SortUtil.sort(duplicates);
		ObjectFormatter<Type> formatter = new TypeInformationFormatter();
		for (List<Type> duplicate : duplicates) {
			PrintUtil.println(output, formatter, duplicate,"", "", "\n");
			PrintUtil.printSeparator(output);
		}
	}

}
