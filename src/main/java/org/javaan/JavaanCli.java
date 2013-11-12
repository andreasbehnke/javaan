package org.javaan;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.text.WordUtils;
import org.javaan.commands.ListClasses;
import org.javaan.commands.ListDuplicates;
import org.javaan.commands.ListInterfaces;
import org.javaan.commands.ShowCallerGraph;
import org.slf4j.LoggerFactory;

/**
 * Javaan Command Line Client
 * @author behnkea
 */
public class JavaanCli {
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(JavaanCli.class);
	
	private static final int MAX_WIDTH = 80;
	
	private static final String USAGE = "Usage:";
	private static final String HELP_COMMAND = "javaan <command> <files> <options>";
	private static final String HELP_HEADER = 
			  "javaan is a tool for static code analysis. It is using byte code analysis to provide "
			+ "informations about the loaded types. There are several sub commands for different tasks. "
			+ "The command name is followed by a list of jar, war or ear files, which should be processed, "
			+ "and options.";
	public static final String HELP_COMMANDS = "supported commands:";
	private static final String HELP_FOOTER = 
			  "Use javaan <command> --help to display detailed options of command.";
	
	private static final String EXCEPTION_MISSING_FILES = "No file list provided";
	private static final String EXCEPTION_UNKNOWN_COMMAND = "Unknown command: %s";
	private static final String EXCEPTION_COULD_NOT_PARSE = "Could not parse command line argumeents: %s";
	private static final String EXCEPTION_COMMAND = "Could not process command";
	
	private final CommandMap commands;
	
	private final String[] args;
	
	public JavaanCli(String[] args, CommandMap commands) {
		this.commands = commands;
		this.args = args;
	}

	public static void main(String[] args) {
		CommandMap commands = new CommandMap();
		commands.addCommand(new ListClasses());
		commands.addCommand(new ListInterfaces());
		commands.addCommand(new ListDuplicates());
		commands.addCommand(new ShowCallerGraph());
		System.exit(new JavaanCli(args, commands).execute().getValue());
	}
	
	private void setLoggerLevel(Level level) {
		Logger logger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			handler.setLevel(level);
		}
		logger.setLevel(level);
	}

	public ReturnCodes execute() {
		if (args.length < 1) {
			printUsage();
			return ReturnCodes.errorParse;
		}
		Command command = commands.getCommand(args[0]);
		boolean withoutCommand = (command == null);
		Options options = new Options();
		options.addOption("h", "help", false, "Display help information for this command");
		options.addOption("v", "verbose", false, "Log verbose output");
		if (!withoutCommand) {
			options = command.buildCommandLineOptions(options);
		}
		try {
			CommandLine cl = new GnuParser().parse(options, args);
			boolean displayHelp = cl.hasOption("h");
			if (displayHelp) {
				printCommandUsage(command, options);
				return ReturnCodes.ok;
			}
			if (!displayHelp && withoutCommand) {
				System.out.println(String.format(EXCEPTION_UNKNOWN_COMMAND, args[0]));
				printUsage();
				return ReturnCodes.errorParse;
			}
			if (cl.hasOption("v")) {
				setLoggerLevel(Level.FINEST);
			} else {
				setLoggerLevel(Level.SEVERE);
			}
			
			String[] params = cl.getArgs();
			if (params.length < 2) {
				System.out.println(EXCEPTION_MISSING_FILES);
				printUsage();
				return ReturnCodes.errorParse;
			}
			String[] files = Arrays.copyOfRange(params, 1, params.length);
			return command.execute(cl, files);
		} catch(ParseException e) {
			System.out.println(String.format(EXCEPTION_COULD_NOT_PARSE, e.getMessage()));
			printCommandUsage(command, options);
			return ReturnCodes.errorParse;
		} catch (Exception e) {
			LOG.error(EXCEPTION_COMMAND, e);
			return ReturnCodes.errorCommand;
		}
	}
	
	private int maxCommandNameLength() {
		int length = 0;
		for (Command command : commands.getCommands()) {
			String name = command.getName();
			if (name.length() > length)	{
				length = name.length();
			}
		}
		return length;
	}
	
	private String createIndent() {
		int width = maxCommandNameLength() + 3;
		StringBuilder buffer = new StringBuilder(width);
		for(int i=0; i < width; i++) {
			buffer.append(' ');
		}
		return buffer.toString();
	}
	
	private String formatCommandName(String name, String indent) {
		return new StringBuilder(indent.length())
			.append(' ').append(name).append(": ")
			.append(indent, 0, indent.length() - name.length() - 3).toString();
	}
	
	private void printParagraph(String content) {
		System.out.println(WordUtils.wrap(content, MAX_WIDTH));
		System.out.println();
	}
	
	public void printUsage() {
		printParagraph(USAGE);
		printParagraph(HELP_COMMAND);
		printParagraph(HELP_HEADER);
		printParagraph(HELP_COMMANDS);
		String indent = createIndent();
		for (Command command : commands.getCommands()) {
			System.out.print(formatCommandName(command.getName(), indent));
			System.out.println(
					WordUtils.wrap(command.getDescription(), 
							MAX_WIDTH - indent.length(), 
							System.lineSeparator() + indent, 
							true));
		}
		System.out.println();
		printParagraph(HELP_FOOTER);
	}
	
	
	private void printCommandUsage(Command command, Options options) {
		if (command == null) {
			printUsage();
		} else {
			new HelpFormatter()
				.printHelp(command.getHelpCommandLine(), command.getDescription(), options, "");
		}
	}
}