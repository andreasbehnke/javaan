package org.javaan;

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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.text.WordUtils;
import org.javaan.commands.*;
import org.javaan.print.PrintUtil;
import org.slf4j.LoggerFactory;

/**
 * Javaan Command Line Client
 * @author behnkea
 */
public class JavaanCli {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(JavaanCli.class);

	private static final int MAX_WIDTH = 120;
	private static final int SEPARATOR_WIDTH = MAX_WIDTH / 2;

	private static final String HELP_USAGE = "usage:";
	private static final String HELP_COMMAND = "javaan <command> <arguments> <options>\njavaan --help\njavaan <command> --help";
	private static final String HELP_HEADER =
			  "javaan is a tool for static code analysis. It is using byte code analysis to provide "
			+ "information about the loaded types. There are several sub commands for different tasks. "
			+ "The command name is followed by a list of jar, war or ear files, which should be processed, "
			+ "and options.";
	public static final String HELP_COMMANDS = "supported commands:";
	private static final String HELP_FOOTER =
			  "Use javaan <command> --help to display detailed options of command.";
	private static final String HELP_COMMAND_DETAILS = "command details:";

	private static final String EXCEPTION_UNKNOWN_COMMAND = "Unknown command: %s";
	private static final String EXCEPTION_COULD_NOT_PARSE = "Could not parse command line arguments: %s";
	private static final String EXCEPTION_COMMAND = "Could not process command";

	private final CommandMap commands;

	private final String[] args;

	private final Writer writer;

	public JavaanCli(String[] args, CommandMap commands, Writer writer) {
		this.commands = commands;
		this.args = args;
		this.writer = writer;
	}

	static CommandMap getCommands() {
		CommandMap commands = new CommandMap();
		commands.addCommand(new ListPackages());
		commands.addCommand(new ListClasses());
		commands.addCommand(new ListInterfaces());
		commands.addCommand(new ListDuplicates());
		commands.addCommand(new ListMissingTypes());
		commands.addCommand(new ShowDependencyCyclesGraph());
		commands.addCommand(new ShowCallerGraph());
		commands.addCommand(new ShowCalleeGraph());
		commands.addCommand(new ShowUsedGraph());
		commands.addCommand(new ShowUsingGraph());
		commands.addCommand(new ShowPackageDependencyCyclesGraph());
		commands.addCommand(new ShowPackageUsedGraph());
		commands.addCommand(new ShowPackageUsingGraph());
		commands.addCommand(new SetOptions());
		commands.addCommand(new ResetOptions());
		commands.addCommand(new Benchmark());
		return commands;
	}

	public static void main(String[] args) throws IOException {
		try (Writer writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8)) {
			ReturnCodes returnCode = new JavaanCli(args, getCommands(), writer).execute();
			writer.flush();
			if (returnCode != ReturnCodes.threadSpawn)
			{
				System.exit(returnCode.getValue());
			}
		}
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
			printUsage(false);
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
			CommandLine cl = new DefaultParser().parse(options, args);
			boolean displayHelp = cl.hasOption("h");
			if (displayHelp && !withoutCommand) {
				printCommandUsage(command, options);
				return ReturnCodes.ok;
			} else if (displayHelp) {
				printUsage(true);
				return ReturnCodes.ok;
			} else if (withoutCommand) {
				PrintUtil.format(writer,EXCEPTION_UNKNOWN_COMMAND, args[0]);
				printUsage(false);
				return ReturnCodes.errorParse;
			}
			if (cl.hasOption("v")) {
				setLoggerLevel(Level.INFO);
			} else {
				setLoggerLevel(Level.WARNING);
			}

			String[] arguments = cl.getArgs();
			arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
			return command.execute(new CommandContext(writer, cl, arguments, new Settings()));
		} catch(ParseException e) {
			PrintUtil.format(writer,EXCEPTION_COULD_NOT_PARSE, e.getMessage());
			if (withoutCommand) {
				printUsage(false);
			} else {
				printCommandUsage(command, options);
			}
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
		return " ".repeat(Math.max(0, width));
	}

	private String formatCommandName(String name, String indent) {
		return ' ' + name + ": " +
				indent.substring(0, indent.length() - name.length() - 3);
	}

	private void printParagraph(String content) {
        PrintUtil.println(writer, WordUtils.wrap(content, MAX_WIDTH));
        PrintUtil.println(writer);
	}

	public void printUsage(boolean printFullHelp) {
		printParagraph(HELP_USAGE);
		printParagraph(HELP_COMMAND);
		printParagraph(HELP_HEADER);
		printParagraph(HELP_COMMANDS);
		String indent = createIndent();
		for (Command command : commands.getCommands()) {
            PrintUtil.print(writer, formatCommandName(command.getName(), indent));
            PrintUtil.println(writer,
                    WordUtils.wrap(command.getDescription(),
							MAX_WIDTH - indent.length(),
							System.lineSeparator() + indent,
							true));
		}
		printParagraph(HELP_FOOTER);
		if (printFullHelp) {
			PrintUtil.println(writer);
			printParagraph(HELP_COMMAND_DETAILS);
			for (Command command : commands.getCommands()) {
				Options options = new Options();
				options = command.buildCommandLineOptions(options);
                PrintUtil.println(writer);
                PrintUtil.println(writer, command.getName());
				printSeparator();
				printCommandUsage(command, options);
			}
		}
	}

	private void printSeparator() {
		String buffer = "-".repeat(SEPARATOR_WIDTH) +
				System.lineSeparator();
        PrintUtil.println(writer, buffer);
	}

	private void printCommandUsage(Command command, Options options) {
		HelpFormatter helpFormatter = new HelpFormatter();
		//helpFormatter.printHelp(command.getHelpCommandLine() + "\n", , options, "");
		helpFormatter.printHelp(
				new PrintWriter(writer),
				MAX_WIDTH,
				command.getHelpCommandLine() + "\n",
				command.getDescription(),
				options,
                HelpFormatter.DEFAULT_LEFT_PAD,
				HelpFormatter.DEFAULT_DESC_PAD,
				"");
	}
}
