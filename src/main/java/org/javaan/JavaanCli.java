package org.javaan;



/**
 * Javaan Command Line Client
 * @author behnkea
 */
public class JavaanCli {
	
	private static final String HELP_COMMAND = "javaan <command> <files> <options>";
	private static final String HELP_DESCRIPTION = 
			  "javaan is a java byte code analyser for static code analysis.\n"
			+ "Use javaan <command> --help to display help message for a specific command.\n"
			+ "The command is followed by a list of jar files which should be processed\n"
			+ "and the command options.";
	private static final String EXCEPTION_MISSING_FILES = "No file list provided";
	private static final String EXCEPTION_UNKNOWN_COMMAND = "Unknown command: %s";
	private static final CommandMap COMMANDS = new CommandMap();
	static {
		COMMANDS.addCommand(new FindEntryMethodsCommand());
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			printUsage();
			return;
		}
		if (args.length < 2) {
			System.out.println(EXCEPTION_MISSING_FILES);
			printUsage();
			return;
		}
		Command command = COMMANDS.getCommand(args[0]);
		if (command == null) {
			System.out.println(String.format(EXCEPTION_UNKNOWN_COMMAND, args[0]));
			printUsage();
			return;
		}
		command.execute(args);
	}

	private static void printUsage() {
		System.out.println();
		System.out.println(HELP_COMMAND);
		System.out.println();
		System.out.println(HELP_DESCRIPTION);
		System.out.println();
		COMMANDS.printHelp();
		System.out.println();
	}
}