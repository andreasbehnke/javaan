package org.javaan.commands;

import org.javaan.Settings;

public class SetOptions extends BaseSetOptionsCommand {

	private final static String NAME = "set";

	private final static String DESCRIPTION = "Makes options persistent across consecutive calls. If no option is provided, lists all persistent options."
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
	protected String getDescriptionFormat() {
		return DESCRIPTION;
	}
	
	@Override
	protected void processOption(Settings settings, String option) {
		settings.enableOption(option);
	}
}
