package org.javaan.commands;

import java.io.OutputStream;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;

public class FindEntryMethods extends BaseCommand {
	
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
	protected void execute(CommandLine commandLine, OutputStream output, List<JavaClass> classes) {
		// TODO Auto-generated method stub
		
	}
}
