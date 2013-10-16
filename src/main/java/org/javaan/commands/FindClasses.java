/**
 * 
 */
package org.javaan.commands;

import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;

/**
 * @author andreasbehnke
 *
 */
public class FindClasses extends BaseCommand {
	
	private final static String NAME = "findClasses";
	
	private final static String DESCRIPTION = "List all classes of the libraries";
	
	private final static String HELP_COMMAND_LINE = "javaan findClasses <files>";
	
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
	protected void execute(CommandLine commandLine, List<JavaClass> classes) {
		// TODO Auto-generated method stub
		
	}

}
