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
		options.addOption("superClasses", false, "For each class list all super classes");
		options.addOption("interfaces", false, "Include all interfaces of libraries");
		return options;
	}
	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<ClassData> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		boolean includeInterfaces = commandLine.hasOption("interfaces");
		if (commandLine.hasOption("superClasses")) {
			printClassesAndSuperClasses(output, classContext, includeInterfaces);
		} else {
			printClasses(output, classContext, includeInterfaces);
		}
	}
	
	public void printClasses(PrintStream output, ClassContext classContext, boolean includeInterfaces) {
		PrintUtil.println(output, SortUtil.sort(classContext.getClasses()), "", "[C]", System.lineSeparator());
		if (includeInterfaces) {
			PrintUtil.println(output, SortUtil.sort(classContext.getInterfaces()), "", "[I]", System.lineSeparator());	
		}
	}

	public void printClassesAndSuperClasses(PrintStream output, ClassContext classContext, boolean includeInterfaces) {
		List<String> classes = SortUtil.sort(classContext.getClasses());
		for (String clazz : classes) {
			PrintUtil.println(output, classContext.getSuperClassHierachy(clazz), "[C]", "", ",");
		}
		if (includeInterfaces) {
			List<String> interfaces = SortUtil.sort(classContext.getInterfaces());
			for (String interfaze : interfaces) {
				List<String> superInterfaces = SortUtil.sort(classContext.getSuperInterfaces(interfaze));
				superInterfaces.add(0, interfaze);
				PrintUtil.println(output, superInterfaces, "[I]", "", ",");
			}
		}
	}
	
}
