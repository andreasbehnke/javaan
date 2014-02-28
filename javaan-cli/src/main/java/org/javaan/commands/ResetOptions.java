package org.javaan.commands;

import org.javaan.Settings;

public class ResetOptions extends BaseSetOptionsCommand {

	private final static String NAME = "reset";

	private final static String DESCRIPTION = "Resets options which have been made persistent with set command. If no option is provided, lists all persistent options."
			+ " The following options can be resetted, separated by whitespace: %s";
	
	private static final String HELP_COMMAND_LINE = "javaan reset <optionlist>";

	@Override
	public String getHelpCommandLine() {
		return HELP_COMMAND_LINE;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected String getDescriptionFormat() {
		return DESCRIPTION;
	}
	
	@Override
	protected void processOption(Settings settings, String option) {
		settings.disableOption(option);
	}
}
