package org.javaan.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.javaan.ReturnCodes;
import org.javaan.model.DuplicatesFinder;
import org.javaan.model.Type;

public abstract class BaseTypeLoadingCommand extends BaseCommand {

	@Override
	public ReturnCodes execute(CommandLine commandLine, String[] files) {
		try {
			List<Type> types = loadTypes(files);
			DuplicatesFinder<Type> finder = new DuplicatesFinder<Type>(types);
			if (finder.hasDuplicates()) {
				LOG.warn("Loaded libraries contain duplicate type definitions!\n"
						+ "Use command \"javaan listDuplicates\" to find out duplicate types.");
				types = new ArrayList<Type>(finder.createUniqueSet());
			}
			execute(commandLine, System.out, types);
		} catch (IOException e) {
			LOG.error("Could not load class files from libraries", e);
			return ReturnCodes.errorCommand;
		}
		return ReturnCodes.ok;
	}
	
	protected abstract void execute(CommandLine commandLine, PrintStream output, List<Type> types);
}
