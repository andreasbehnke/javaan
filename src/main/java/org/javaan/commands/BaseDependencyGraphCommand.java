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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;
import org.javaan.print.ObjectFormatter;
import org.javaan.print.TypeFormatter;

/**
 * Base command for all class dependency commands
 */
public abstract class BaseDependencyGraphCommand extends BaseGraphCommand<Type> {

	@Override
	public Options buildCommandLineOptions(Options options) {
		options.addOption(StandardOptions.FILTER);
		options.addOption(StandardOptions.LEAVES);
		return options;
	}

	@Override
	protected String filterCriteria(CommandLine commandLine) {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	@Override
	protected ObjectFormatter<Type> getFormatter() {
		return new TypeFormatter();
	}

	@Override
	protected Collection<Type> getInput(ClassContext classContext, CallGraph callGraph, String filterCriteria) {
		Collection<Type> types = new ArrayList<Type>();
		types.addAll(classContext.getClasses());
		types.addAll(classContext.getInterfaces());
		return SortUtil.sort(FilterUtil.filter(types, new NameMatcher<Type>(filterCriteria)));
	}

	
}
