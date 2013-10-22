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

public class ListClasses extends BaseCommand {
	
	private final static String NAME = "listClasses";
	
	private final static String DESCRIPTION = "List all classes of the libraries";
	
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
		additionalInformation.addOption(new Option("sc", "superClasses", false, "For each class list the class hierachy of super classes"))
			.addOption(new Option("i", "interfaces", false, "For each class list all implemented interfaces"));
		options.addOptionGroup(additionalInformation);
		return options;
	}
	
	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		if (commandLine.hasOption("sc")) {
			printClassesAndSuperClasses(output, classContext);
		} else if (commandLine.hasOption("i")) {
			printClassesAndInterfaces(output, classContext);
		} else {
			printClasses(output, classContext);
		}
	}
	
	public void printClasses(PrintStream output, ClassContext classContext) {
		PrintUtil.println(output, SortUtil.sort(classContext.getClasses()), "", "[C]", System.lineSeparator());
	}

	public void printClassesAndSuperClasses(PrintStream output, ClassContext classContext) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getSuperClassHierachy(clazz), "", "[C]", " --> ");
		}
	}
	
	public void printClassesAndInterfaces(PrintStream output, ClassContext classContext) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getInterfacesOfClass(clazz), "[C]" + clazz + ": ", "[I]", ", ");
		}
	}
}
