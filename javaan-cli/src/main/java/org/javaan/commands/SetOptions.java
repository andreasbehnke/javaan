package org.javaan.commands;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.Settings;

public class SetOptions extends BaseCommand {

	private final static String NAME = "set";

	private final static String DESCRIPTION = "Makes options persistent across consequtive calls. If no option is provided, lists all persisted options."
			+ " The following options can be set, separated by whitespace: %s";
	
	private static final String HELP_COMMAND_LINE = "javaan set <optionlist>";

	@Override
	public String getHelpCommandLine() {
		return HELP_COMMAND_LINE;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		StringBuilder buffer = new StringBuilder();
		for (Option option : StandardOptions.PERSISTENT_OPTIONS) {
			buffer.append(option.getOpt())
				.append(" (")
				.append(option.getLongOpt())
				.append(") ");
		}
		return String.format(DESCRIPTION, buffer);
	}

	@Override
	public Options buildCommandLineOptions(Options options) {
		return options;
	}
	
	private boolean optionExsists(String option) {
		for (Option optionObj : StandardOptions.PERSISTENT_OPTIONS) {
			if (optionObj.getOpt().equals(option)) {
				return true;
			}
		}
		return false;
	}
	
	private ReturnCodes processOptions(CommandContext commandContext, String[] options) {
		for (String option : options) {
			if (!optionExsists(option)) {
				LOG.error("Unknown option: " + option);
				return ReturnCodes.errorCommand;
			}
		}
		Settings settings = commandContext.getSettings();
		for (String option : options) {
			settings.enableOption(option);
		}
		return ReturnCodes.ok;
	}
	
	private ReturnCodes listPersistentOptions(CommandContext commandContext) {
		Settings settings = commandContext.getSettings();
		System.out.println("The following options are set:");
		for (Option optionObj : StandardOptions.PERSISTENT_OPTIONS) {
			if (settings.isOptionEnabled(optionObj.getOpt())) {
				System.out.println(optionObj.getOpt());
			}
		}
		return ReturnCodes.ok;
	}

	@Override
	public ReturnCodes execute(CommandContext commandContext) {
		String[] options = commandContext.getArguments();
		if (options.length == 0) {
			return listPersistentOptions(commandContext);
		} else {
			return processOptions(commandContext, options);
		}
	}

}
