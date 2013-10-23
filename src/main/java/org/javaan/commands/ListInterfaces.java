package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
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
		OptionGroup additionalInformation = new OptionGroup();
		additionalInformation
			.addOption(new Option(OptionName.SUPER, "superInterfaces", false, "For each interface list all super interfaces"))
			.addOption(new Option(OptionName.SPECIALIZATIONS, "specializations", false, "For each interface list all specializations"))
			.addOption(new Option(OptionName.IMPLEMENTATION, "implementations", false, "For each interface list all implementing classes"));
		options.addOptionGroup(additionalInformation);
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		if (commandLine.hasOption(OptionName.SUPER)) {
			printInterfacesAndSuperInterfaces(output, classContext);
		} else if (commandLine.hasOption(OptionName.SPECIALIZATIONS)) {
			printInterfacesAndSpecializations(output, classContext);
		} else if (commandLine.hasOption(OptionName.IMPLEMENTATION)) {
			printInterfacesAndImplementations(output, classContext);
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
			PrintUtil.println(output, classContext.getSuperInterfaces(interfaceName), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}
	
	public void printInterfacesAndSpecializations(PrintStream output, ClassContext classContext) {
		List<String> interfaces = SortUtil.sort(classContext.getInterfaces());
		for (String interfaceName : interfaces) {	
			PrintUtil.println(output, classContext.getSpecializationOfInterface(interfaceName), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}

	public void printInterfacesAndImplementations(PrintStream output, ClassContext classContext) {
		List<String> interfaces = SortUtil.sort(classContext.getInterfaces());
		for (String interfaceName : interfaces) {	
			PrintUtil.println(output, classContext.getImplementations(interfaceName), "[I]" + interfaceName + ": ", "[C]", ", ");
		}
	}
}
