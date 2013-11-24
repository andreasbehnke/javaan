package org.javaan.commands;

import java.io.IOException;
import java.util.List;

import org.javaan.Command;
import org.javaan.bytecode.JarFileLoader;
import org.javaan.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand implements Command {
	
	private final static String LOGGER_NAME = "Command";

	protected final static Logger LOG = LoggerFactory.getLogger(LOGGER_NAME);

	private static final String HELP_COMMAND_LINE = "javaan %s <files> <options>";

	public BaseCommand() {
		super();
	}

	@Override
	public String getHelpCommandLine() {
		return String.format(HELP_COMMAND_LINE, getName());
	}
	
	protected List<Type> loadTypes(String[] files) throws IOException {
		LOG.info("Processing jar files...");
		JarFileLoader loader = new JarFileLoader();
		List<Type> types = loader.loadJavaClasses(files);
		LOG.info("Loaded {} class files", types.size());
		return types;
	}
}