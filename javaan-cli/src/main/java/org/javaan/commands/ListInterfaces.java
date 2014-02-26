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
import org.javaan.CommandContext;
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
	protected void execute(PrintStream output, CommandContext context, List<Type> types) {
		this.classContext = new ClassContextBuilder(types).build();
		Collection<Interface> interfaces = SortUtil.sort(classContext.getInterfaces());
		if (context.hasFilterCriteria()) {
			String criteria = context.getFilterCriteria();
			interfaces = FilterUtil.filter(interfaces, new NameMatcher<Interface>(criteria)); 
		}
		switch (context.getAdditionalTypeInformation()) {
		case SUPER_CLASSES:
			printInterfacesAndSuperInterfaces(output, interfaces);
			break;
		case SPECIALIZATIONS:
			printInterfacesAndSpecializations(output, interfaces);
			break;
		case IMPLEMENTATIONS:
			printInterfacesAndImplementations(output, interfaces);
			break;
		case METHODS:
			printInterfacesAndMethods(output, interfaces);
			break;
		case VIRTUAL_METHODS:
			printInterfacesAndVirtualMethods(output, interfaces);
			break;
		default:
			printInterfaces(output, interfaces);
			break;
		}
	}
	
	private void printInterfaces(PrintStream output, Collection<Interface> interfaces) {
		PrintUtil.println(output, interfaces, "", "[I]", System.lineSeparator());
	}

	private void printInterfacesAndSuperInterfaces(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {	
			PrintUtil.println(output, SortUtil.sort(classContext.getSuperInterfaces(interfaceName)), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}
	
	private void printInterfacesAndSpecializations(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {	
			PrintUtil.println(output, SortUtil.sort(classContext.getSpecializationOfInterface(interfaceName)), "[I]" + interfaceName + ": ", "[I]", ", ");
		}
	}

	private void printInterfacesAndImplementations(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, SortUtil.sort(classContext.getImplementations(interfaceName)), "[I]" + interfaceName + ": ", "[C]", ", ");
		}
	}
	
	private void printInterfacesAndMethods(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getMethods(interfaceName)), "[I]" + interfaceName + ": ", "\n\t", ", ");
		}
	}
	
	private void printInterfacesAndVirtualMethods(PrintStream output, Collection<Interface> interfaces) {
		for (Interface interfaceName : interfaces) {
			PrintUtil.println(output, new MethodFormatter(), SortUtil.sort(classContext.getVirtualMethods(interfaceName)), "[I]" + interfaceName + ": ", "\n\t", ", ");
		}
	}
}
