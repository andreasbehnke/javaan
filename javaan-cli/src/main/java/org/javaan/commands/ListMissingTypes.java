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

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;

public class ListMissingTypes extends BaseTypeLoadingCommand {

	private final static String NAME = "missing-types";

	private final static String DESCRIPTION = "List types which are referenced by loaded types but could not be resolved.";

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
	protected void execute(PrintStream output, List<Type> types) {
		ClassContextBuilder classContextBuilder = new ClassContextBuilder(types);
		ClassContext classContext = classContextBuilder.build();
		Set<String> missingTypes = classContextBuilder.getMissingTypes();
		CallGraphBuilder callGraphBuilder = new CallGraphBuilder(classContext, false, false);
		callGraphBuilder.build();
		missingTypes.addAll(callGraphBuilder.getMissingTypes());
		List<String> sortedMissingTypes = SortUtil.sort(missingTypes);
		for (String missingType : sortedMissingTypes) {
			output.println(missingType);
		}
	}

}
