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

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.javaan.CommandContext;
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
	protected void execute(CommandContext context, List<Type> types) {
		Writer writer = context.getWriter();
		this.classContext = new ClassContextBuilder().build(types);
		Collection<Clazz> classes = classContext.getClasses();
		if (context.hasFilterCriteria()) {
			String criteria = context.getFilterCriteria();
			classes = FilterUtil.filter(classes, new NameMatcher<>(criteria));
		}
		classes = SortUtil.sort(classes);
		switch (context.getAdditionalTypeInformation()) {
		case SUPER_CLASSES:
			printClassesAndSuperClasses(writer, classes);
			break;
		case SPECIALIZATIONS:
			printClassesAndSpecializations(writer, classes);
			break;
		case INTERFACES:
			printClassesAndInterfaces(writer, classes);
			break;
		case METHODS:
			printClassesAndMethods(writer, classes);
			break;
		case VIRTUAL_METHODS:
			printClassesAndVirtualMethods(writer, classes);
			break;
		default:
			printClasses(writer, classes);
			break;
		}
	}

	private void printClasses(Writer writer, Collection<Clazz> classes) {
		PrintUtil.println(writer, classes, "", "[C]", System.lineSeparator());
	}

	private void printClassesAndSuperClasses(Writer writer, Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(writer, classContext.getSuperClassHierarchy(clazz), "", "[C]", " --> ");
		}
	}

	private void printClassesAndSpecializations(Writer writer,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(writer, classContext.getSpecializationsOfClass(clazz), "[C]" + clazz + ": ", "[C]", ", ");
		}
	}

	private void printClassesAndInterfaces(Writer writer,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(writer, classContext.getInterfacesOfClass(clazz), "[C]" + clazz + ": ", "[I]", ", ");
		}
	}

	private void printClassesAndMethods(Writer writer,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(writer, new MethodFormatter(), SortUtil.sort(classContext.getMethods(clazz)), "[C]" + clazz + ": ", "\n\t", ", ");
		}
	}

	private void printClassesAndVirtualMethods(Writer writer,  Collection<Clazz> classes) {
		for (Clazz clazz : classes) {
			PrintUtil.println(writer, new MethodFormatter(), SortUtil.sort(classContext.getVirtualMethods(clazz)), "[C]" + clazz + ": ", "\n\t", ", ");
		}
	}
}
