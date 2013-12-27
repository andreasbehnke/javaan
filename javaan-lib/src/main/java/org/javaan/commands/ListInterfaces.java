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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.model.ClassContext;
import org.javaan.model.Interface;
import org.javaan.model.Type;
import org.javaan.print.MethodFormatter;
import org.javaan.print.PrintUtil;

public class ListInterfaces extends BaseTypeLoadingCommand {
	
	private final static String NAME = "interfaces";
	
	private final static String DESCRIPTION = "List all interfaces of the libraries";
	
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
			.addOption(StandardOptions.IMPLEMENTATIONS)
			.addOption(StandardOptions.METHODS)
			.addOption(StandardOptions.VIRTUAL_METHODS);
		options.addOptionGroup(additionalInformation);
		options.addOption(StandardOptions.FILTER);
		return options;
	}

	@Override
	protected void execute(CommandLine commandLine, PrintStream output, List<Type> types) {
		this.classContext = new ClassContextBuilder(types).build();
		Collection<Interface> interfaces = SortUtil.sort(classContext.getInterfaces());
		if (commandLine.hasOption(StandardOptions.OPT_FILTER)) {
			String criteria = commandLine.getOptionValue(StandardOptions.OPT_FILTER);
			interfaces = FilterUtil.filter(interfaces, new NameMatcher<Interface>(criteria)); 
		}
		if (commandLine.hasOption(StandardOptions.OPT_SUPER)) {
			printInterfacesAndSuperInterfaces(output, interfaces);
		} else if (commandLine.hasOption(StandardOptions.OPT_SPECIALIZATIONS)) {
			printInterfacesAndSpecializations(output, interfaces);
		} else if (commandLine.hasOption(StandardOptions.OPT_IMPLEMENTATIONS)) {
			printInterfacesAndImplementations(output, interfaces);
		} else if (commandLine.hasOption(StandardOptions.OPT_METHODS)) {
			printInterfacesAndMethods(output, interfaces);
		} else if (commandLine.hasOption(StandardOptions.OPT_VIRTUAL_METHODS)) {
			printInterfacesAndVirtualMethods(output, interfaces);
		} else {
			printInterfaces(output, interfaces);
		}
	}
	
	public void printInterfaces(PrintStream output, Collection<Interface> interfaces) {
		PrintUtil.println(output, interfaces, "", "[I]", System.lineSeparator());
	}

	public void printInterfacesAndSuperInterfaces(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {	
			PrintUtil.println(output, SortUtil.sort(classContext.getSuperInterfaces(interfaceName)), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}
	
	public void printInterfacesAndSpecializations(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {	
			PrintUtil.println(output, SortUtil.sort(classContext.getSpecializationOfInterface(interfaceName)), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}

	public void printInterfacesAndImplementations(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, SortUtil.sort(classContext.getImplementations(interfaceName)), "[I]" + interfaceName + ": ", "[C]", ", ");
		}
	}
	
	public void printInterfacesAndMethods(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getMethods(interfaceName)), "[I]" + interfaceName + ": ", "\n\t", ", ");
		}
	}
	
	public void printInterfacesAndVirtualMethods(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getVirtualMethods(interfaceName)), "[I]" + interfaceName + ": ", "\n\t", ", ");
		}
	}
}
