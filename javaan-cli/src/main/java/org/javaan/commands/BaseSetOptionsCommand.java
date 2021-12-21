package org.javaan.commands;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.Settings;


public abstract class BaseSetOptionsCommand extends BaseCommand {

	protected abstract String getDescriptionFormat();

	@Override
	public String getDescription() {
		StringBuilder buffer = new StringBuilder();
		for (Option option : StandardOptions.PERSISTENT_OPTIONS) {
			buffer.append(option.getOpt())
				.append(" (")
				.append(option.getLongOpt())
				.append(") ");
		}
		return String.format(getDescriptionFormat(), buffer);
	}

	@Override
	public Options buildCommandLineOptions(Options options) {
		return options;
	}


	private boolean optionExists(String option) {
		for (Option optionObj : StandardOptions.PERSISTENT_OPTIONS) {
			if (optionObj.getOpt().equals(option)) {
				return true;
			}
		}
		return false;
	}

	protected abstract void processOption(Settings settings, String option);

	private ReturnCodes processOptions(CommandContext commandContext, String[] options) {
		for (String option : options) {
			if (!optionExists(option)) {
				LOG.error("Unknown option: " + option);
				return ReturnCodes.errorCommand;
			}
		}
		Settings settings = commandContext.getSettings();
		for (String option : options) {
			processOption(settings, option);
		}
		return ReturnCodes.ok;
	}

	private void listPersistentOptions(CommandContext commandContext) {
		Settings settings = commandContext.getSettings();
		System.out.println("The following options are set:");
		for (Option optionObj : StandardOptions.PERSISTENT_OPTIONS) {
			if (settings.isOptionEnabled(optionObj.getOpt())) {
				System.out.println(optionObj.getOpt());
			}
		}
	}

	@Override
	public ReturnCodes execute(CommandContext commandContext) {
		String[] options = commandContext.getArguments();
		if (options.length == 0) {
			listPersistentOptions(commandContext);
			return ReturnCodes.ok;
		} else {
			return processOptions(commandContext, options);
		}
	}
}
