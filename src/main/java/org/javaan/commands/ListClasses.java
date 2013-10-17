/**
 * 
 */
package org.javaan.commands;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.javaan.BaseCommand;
import org.javaan.ClassContext;
import org.javaan.ClassContextBuilder;
import org.javaan.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andreasbehnke
 *
 */
public class ListClasses extends BaseCommand {
	
	private final static Logger LOG = LoggerFactory.getLogger(ListClasses.class);
	
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
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<JavaClass> classes) {
		ClassContext classContext = new ClassContextBuilder(classes).build();
		if (commandLine.hasOption("superClasses")) {
			printClassesAndSuperClasses(output, classContext);
		} else {
			printClasses(output, classContext);
		}
	}
	
	public void printClasses(PrintStream output, ClassContext classContext) {
		PrintUtil.println(output, classContext.getClasses(), System.lineSeparator());
	}

	public void printClassesAndSuperClasses(PrintStream output, ClassContext classContext) {
		Set<String> classes = classContext.getClasses();
		for (String clazz : classes) {
			LOG.info("Processing class {}", clazz);
			PrintUtil.println(output, classContext.getSuperClasses(clazz), ",");
		}
	}
	
}
