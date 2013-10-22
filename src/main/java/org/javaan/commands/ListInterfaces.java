package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;
import org.javaan.ClassContext;
import org.javaan.ClassContextBuilder;
import org.javaan.ClassData;
import org.javaan.PrintUtil;
import org.javaan.SortUtil;

public class ListInterfaces extends BaseCommand {
	
	private final static String NAME = "listInterfaces";
	
	private final static String DESCRIPTION = "List all interfaces of the libraries";
	
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
		options.addOption("si", "superInterfaces", false, "For each interface list all super interfaces");
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		if (commandLine.hasOption("si")) {
			printInterfacesAndSuperInterfaces(output, classContext);
		} else {
			printInterfaces(output, classContext);
		}
	}
	
	public void printInterfaces(PrintStream output, ClassContext classContext) {
		PrintUtil.println(output, SortUtil.sort(classContext.getInterfaces()), "", "[I]", System.lineSeparator());
	}

	public void printInterfacesAndSuperInterfaces(PrintStream output, ClassContext classContext) {
		List<String> interfaces = SortUtil.sort(classContext.getInterfaces());
		for (String interfaceName : interfaces) {	
			PrintUtil.println(output, classContext.getSuperInterfaces(interfaceName), "[I]" + interfaceName, "", ", ");
		}
	}

}
