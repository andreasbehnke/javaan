package org.javaan.commands;

import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;

public class SetOptions extends BaseCommand {

	private final static String NAME = "set";

	private final static String DESCRIPTION = "Makes options persistent across consequtive calls. If no option is provided, lists all persisted options.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Options buildCommandLineOptions(Options options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnCodes execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		return ReturnCodes.ok;
	}

}
