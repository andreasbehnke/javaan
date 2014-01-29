package org.javaan.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.Options;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Package;
import org.javaan.model.Type;
import org.javaan.print.PrintUtil;

public class ListPackages extends BaseTypeLoadingCommand {

	private final static String NAME = "packages";
	
	private final static String DESCRIPTION = "List all packages of the libraries";
	
	private ClassContext classContext;
	
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
		options.addOption(StandardOptions.FILTER);
		return options;
	}

	@Override
	protected void execute(PrintStream output, List<Type> types) {
		this.classContext = new ClassContextBuilder(types).build();
		Collection<Package> packages = classContext.getPackages();
		if (commandLine.hasOption(StandardOptions.OPT_FILTER)) {
			String criteria = commandLine.getOptionValue(StandardOptions.OPT_FILTER);
			packages = FilterUtil.filter(packages, new NameMatcher<Package>(criteria)); 
		}
		packages = SortUtil.sort(packages);
		PrintUtil.println(output, packages, "", "[P]", System.lineSeparator());
	}

}
