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

import org.apache.commons.cli.Option;

public interface StandardOptions {

	static final String OPT_IMPLEMENTATIONS = "impl";

	static final String OPT_SPECIALIZATIONS = "spec";

	static final String OPT_SUPER = "s";
	
	static final String OPT_INTERFACES = "i";
	
	static final String OPT_METHODS = "m";
	
	static final String OPT_VIRTUAL_METHODS = "vm";
	
	static final String OPT_FILTER = "f";
	
	static final String OPT_METHOD = "method";
	
	static final String OPT_LEAVES = "l";
	
	static final Option SUPER_TYPES = new Option(OPT_SUPER, "super", false, "For each type, list the hierachy of super types");
	
	static final Option SPECIALIZATIONS = new Option(OPT_SPECIALIZATIONS, "specializations", false, "For each type, list specialization types");
	
	static final Option INTERFACES = new Option(OPT_INTERFACES, "interfaces", false, "For each class, list all implemented interfaces");
	
	static final Option IMPLEMENTATIONS = new Option(OPT_IMPLEMENTATIONS, "implementations", false, "For each interfaces, list all implementing classes.");
	
	static final Option METHODS = new Option(OPT_METHODS, "methods", false, "For each type, list methods");
	
	static final Option VIRTUAL_METHODS = new Option(OPT_VIRTUAL_METHODS, "virtualMethods", false, "For each type, list all methods and all inherited (virtual) methods");

	static final Option FILTER = new Option(OPT_FILTER, "filter", true, "Filter types by <arg>. All types which contain <arg> in name will match.");

	static final Option METHOD = new Option(OPT_METHOD, true, "Filter methods by <arg>. The format of a matching method is '<canonical class name> - <methodname><signature>'. "
			+ "All methods which contain <arg> in name will match.");

	static final Option LEAVES = new Option(OPT_LEAVES, "leaves", false, "Only display leaf elements of graph.");
}