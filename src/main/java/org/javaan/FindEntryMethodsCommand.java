package org.javaan;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class FindEntryMethodsCommand implements Command {
	
	private final static String NAME = "findEntryMethods";
	
	private final static String HELP_COMMAND_LINE = "javaan findEntryMethods <files> <options>";
	
	private final static String DESCRIPTION = "Finds all entry methods of the libraries. An entry method is a method which is not "
			+ "being used within the library. Use option --method to find entry methods to a specific method.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	@Override
	public String getHelpCommandLine() {
		return HELP_COMMAND_LINE;
	}
	
	@Override
	public Options buildCommandLineOptions(Options options) {
		return options;
	}

	@Override
	public void execute(CommandLine commandLine, String[] files) {
		// TODO Auto-generated method stub
		
	}

}
