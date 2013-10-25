package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;
import org.javaan.ClassContextBuilder;
import org.javaan.PrintUtil;
import org.javaan.SortUtil;
import org.javaan.model.ClassContext;
import org.javaan.model.ClassData;

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
		additionalInformation
			.addOption(new Option(OptionName.SUPER, "superClasses", false, "For each class list the class hierachy of super classes"))
			.addOption(new Option(OptionName.SPECIALIZATIONS, "specializations", false, "For each class list specialization classes"))
			.addOption(new Option(OptionName.INTERFACES, "interfaces", false, "For each class list all implemented interfaces"))
			.addOption(new Option(OptionName.METHODS, "methods", false, "For each class list all virtual methods"));
		options.addOptionGroup(additionalInformation);
		return options;
	}
	
	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		if (commandLine.hasOption(OptionName.SUPER)) {
			printClassesAndSuperClasses(output, classContext);
		} else if (commandLine.hasOption(OptionName.SPECIALIZATIONS)) {
			printClassesAndSpecializations(output, classContext);
		} else if (commandLine.hasOption(OptionName.INTERFACES)) {
			printClassesAndInterfaces(output, classContext);
		} else if (commandLine.hasOption(OptionName.METHODS)) {
			printClassesAndMethods(output, classContext);
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
	
	public void printClassesAndSpecializations(PrintStream output, ClassContext classContext) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getSpecializationsOfClass(clazz), "[C]" + clazz + ": ", "[C]", ", ");
		}
	}
	
	public void printClassesAndInterfaces(PrintStream output, ClassContext classContext) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getInterfacesOfClass(clazz), "[C]" + clazz + ": ", "[I]", ", ");
		}
	}
	
	public void printClassesAndMethods(PrintStream output, ClassContext classContext) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getMethodsOfType(clazz), "[C]" + clazz + ": ", "\n\t[M]", ", ");
		}
	}
}
