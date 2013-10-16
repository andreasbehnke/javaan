/**
 * 
 */
package org.javaan.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.javaan.BaseCommand;
import org.javaan.ClassContext;
import org.javaan.ClassContextBuilder;

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
	protected void execute(CommandLine commandLine, OutputStream output, List<JavaClass> classes) throws IOException {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		IOUtils.writeLines(classContext.getClasses(), System.lineSeparator(), output);
	}

}
