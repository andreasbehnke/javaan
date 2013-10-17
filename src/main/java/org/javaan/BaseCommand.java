package org.javaan;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand implements Command {
	
	private final static Logger LOG = LoggerFactory.getLogger(BaseCommand.class);

	private static final String HELP_COMMAND_LINE = "javaan %s <files> <options>";

	public BaseCommand() {
		super();
	}

	@Override
	public String getHelpCommandLine() {
		return String.format(HELP_COMMAND_LINE, getName());
	}

	@Override
	public ReturnCodes execute(CommandLine commandLine, String[] files) {
		LOG.info("Processing jar files...");
		JarFileLoader loader = new JarFileLoader();
		try {
			execute(commandLine, System.out, loader.loadJavaClasses(files));
		} catch (IOException e) {
			LOG.error("Could not process command", e);
			return ReturnCodes.errorCommand;
		}
		return ReturnCodes.ok;
	}
	
	protected abstract void execute(CommandLine commandLine, PrintStream output, List<JavaClass> classes);

}