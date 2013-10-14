package org.javaan;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface Command {

	String getName();
	
	String getDescription();
	
	String getHelpCommandLine();
	
	Options buildCommandLineOptions(Options options);
	
	void execute(CommandLine commandLine, String[] files);
}
