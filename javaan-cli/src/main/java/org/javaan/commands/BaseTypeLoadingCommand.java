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

import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.model.DuplicatesFinder;
import org.javaan.model.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all commands which need a unique set of types being loaded.
 * A warning message is shown if the loaded types contain duplicates.
 */
public abstract class BaseTypeLoadingCommand extends BaseCommand {

	@Override
	public ReturnCodes execute(CommandContext commandContext) {
		try {
			List<Type> types = loadTypes(commandContext.getArguments());
			if (types == null) {
				return ReturnCodes.errorParse;
			}
			DuplicatesFinder<Type> finder = new DuplicatesFinder<>(types);
			if (finder.hasDuplicates()) {
				LOG.warn("Loaded libraries contain duplicate type definitions!\n"
						+ "Use command \"javaan listDuplicates\" to find out duplicate types.");
				types = new ArrayList<>(finder.createUniqueSet());
			}
			execute(commandContext, types);
		} catch (IOException e) {
			LOG.error("Could not load class files from libraries", e);
			return ReturnCodes.errorCommand;
		}
		return commandContext.getReturnCode();
	}

	protected abstract void execute(CommandContext commandContext, List<Type> types);
}
