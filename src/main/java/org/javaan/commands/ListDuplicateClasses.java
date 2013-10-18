package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;
import org.javaan.ClassData;
import org.javaan.DuplicatesFinder;
import org.javaan.PrintUtil;
import org.javaan.SortUtil;

public class ListDuplicateClasses extends BaseCommand {
	
private final static String NAME = "listDuplicateClasses";
	
	private final static String DESCRIPTION = "List all duplicate classes of the libraries being loaded. Classes are duplications when they have same canonical name, they may vary in bytecode and location.";
	
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
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		List<List<ClassData>> duplicates = new DuplicatesFinder(classes).find();
		SortUtil.sort(duplicates);
		for (List<ClassData> duplicate : duplicates) {
			PrintUtil.println(output, duplicate, "\n");
			output.println();
		}
	}

}
