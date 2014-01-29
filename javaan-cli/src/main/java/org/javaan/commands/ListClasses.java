package org.javaan.commands;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Type;
import org.javaan.print.MethodFormatter;
import org.javaan.print.PrintUtil;

public class ListClasses extends BaseTypeLoadingCommand {
	
	private final static String NAME = "classes";
	
	private final static String DESCRIPTION = "List all classes of the libraries";
	
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
		OptionGroup additionalInformation = new OptionGroup();
		additionalInformation
			.addOption(StandardOptions.SUPER_TYPES)
			.addOption(StandardOptions.SPECIALIZATIONS)
			.addOption(StandardOptions.INTERFACES)
			.addOption(StandardOptions.METHODS)
			.addOption(StandardOptions.VIRTUAL_METHODS);
		options.addOptionGroup(additionalInformation);
		options.addOption(StandardOptions.FILTER);
		return options;
	}

	@Override
	protected void execute(PrintStream output, List<Type> types) {
		this.classContext = new ClassContextBuilder(types).build();
		Collection<Clazz> classes = classContext.getClasses();
		if (commandLine.hasOption(StandardOptions.OPT_FILTER)) {
			String criteria = commandLine.getOptionValue(StandardOptions.OPT_FILTER);
			classes = FilterUtil.filter(classes, new NameMatcher<Clazz>(criteria)); 
		}
		classes = SortUtil.sort(classes);
		if (commandLine.hasOption(StandardOptions.OPT_SUPER)) {
			printClassesAndSuperClasses(output, classes);
		} else if (commandLine.hasOption(StandardOptions.OPT_SPECIALIZATIONS)) {
			printClassesAndSpecializations(output, classes);
		} else if (commandLine.hasOption(StandardOptions.OPT_INTERFACES)) {
			printClassesAndInterfaces(output, classes);
		} else if (commandLine.hasOption(StandardOptions.OPT_METHODS)) {
			printClassesAndMethods(output, classes);
		} else if (commandLine.hasOption(StandardOptions.OPT_VIRTUAL_METHODS)) {
			printClassesAndVirtualMethods(output, classes);
		} else {
			printClasses(output, classes);
		}
	}
	
	public void printClasses(PrintStream output, Collection<Clazz> classes) {
		PrintUtil.println(output, classes, "", "[C]", System.lineSeparator());
	}

	public void printClassesAndSuperClasses(PrintStream output, Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(output, classContext.getSuperClassHierachy(clazz), "", "[C]", " --> ");
		}
	}
	
	public void printClassesAndSpecializations(PrintStream output,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(output, classContext.getSpecializationsOfClass(clazz), "[C]" + clazz + ": ", "[C]", ", ");
		}
	}
	
	public void printClassesAndInterfaces(PrintStream output,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(output, classContext.getInterfacesOfClass(clazz), "[C]" + clazz + ": ", "[I]", ", ");
		}
	}
	
	public void printClassesAndMethods(PrintStream output,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getMethods(clazz)), "[C]" + clazz + ": ", "\n\t", ", ");
		}
	}
	
	public void printClassesAndVirtualMethods(PrintStream output,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getVirtualMethods(clazz)), "[C]" + clazz + ": ", "\n\t", ", ");
		}
	}
}
