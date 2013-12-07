package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Type;

public class ListMissingTypes extends BaseTypeLoadingCommand {

	private final static String NAME = "missingTypes";

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
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		ClassContextBuilder classContextBuilder = new ClassContextBuilder(types);
		ClassContext classContext = classContextBuilder.build();
		Set<String> missingTypes = classContextBuilder.getMissingTypes();
		CallGraphBuilder callGraphBuilder = new CallGraphBuilder(classContext);
		callGraphBuilder.build();
		missingTypes.addAll(callGraphBuilder.getMissingTypes());
		List<String> sortedMissingTypes = SortUtil.sort(missingTypes);
		for (String missingType : sortedMissingTypes) {
			output.println(missingType);
		}
	}

}
